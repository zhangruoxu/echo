package monkey;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

import org.jgrapht.graph.DirectedPseudograph;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import monkey.event.Event;
import monkey.random.RandomEventSource;
import monkey.util.AppInfoWrapper;
import monkey.util.Env;
import monkey.util.Logcat;
import monkey.util.TestingOptions;
import reduction.DijkstraShortestPathFinder;
import reduction.PathEventCollector;
import reduction.TTGReduction;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;
import reduction.ttg.TestingTraceGraph;
import reduction.ttg.node.NormalStateFactory;
import reduction.util.TTGReductionHelper;
import util.Config;
import util.Log;
import util.Timer;
import util.graph.TTGWriter;

/**
 * @author yifei
 * The main class for Appium testing
 */

public class Main {
	private static final String APPIUM_URL = "http://0.0.0.0:4723/wd/hub";

	public static void main(String[] args) {
		Config.init(null);
		reset();
		TestingOptions.v().processOptions(args);
		// Test one app for each time
		AppInfoWrapper appInfo = new AppInfoWrapper(TestingOptions.v().getAppPaths().get(0));
		System.out.println("Test app " + appInfo.getPkgName());
		System.out.println("Options: ");
		System.out.println(TestingOptions.v().toString());
		// clean the output
		appInfo.cleanOutputDirectory();
		Timer timer = new Timer();
		timer.start();
		testingApp(appInfo, (info, env) -> {
			// Clean old logcat output.
			Logcat.clean();
			try(PrintStream printStream = new PrintStream(new File(appInfo.getOutputDirectory(), "output.txt"))) {
				Log.init(printStream);
				// Run random testing
				RandomEventSource eventSource = new RandomEventSource(info, env, TestingOptions.v().getSeed());
				env.addEventSource(eventSource);
				eventSource.runTestingCycles();
				timer.stop();
				Log.println("# Time: " + timer.getDurationInSecond() + " s.");
				// Serialize the graph
				Log.println("# Serialize the TTG.");
				File graphFile = new File(appInfo.getOutputDirectory(), "graph");
				TTGWriter.serializeTTG(graphFile);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		});
		if(TestingOptions.v().isReplay()) {
			System.out.println("Try to replay the error.");
			replay(appInfo, TestingTraceGraph.v().getTTG());
		}
	}

	/**
	 * Setup testing session
	 */
	private static Env setUp(AppInfoWrapper appInfo) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appInfo.getPkgName());
		capabilities.setCapability(MobileCapabilityType.APP, appInfo.getAppPath());
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		AndroidDriver<AndroidElement> driver = null;
		try {
			driver = new AndroidDriver<>(new URL(APPIUM_URL), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		Env env = new Env(driver);
		// init Logcat
		Logcat.init(appInfo, env);
		return env;
	}

	/**
	 * Setup the testing with package name and launchable activity name
	 */
	private static Env setUp(String pkgName, String actName) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, pkgName);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, actName);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		AndroidDriver<AndroidElement> driver = null;
		try {
			driver = new AndroidDriver<>(new URL(APPIUM_URL), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		Env env = new Env(driver);
		// init Logcat
		Logcat.init(null, env);
		return env;
	}

	// Reset testing tool
	private static void reset() {
		// discard old TTG and its nodes
		TestingTraceGraph.reset();
		NormalStateFactory.reset();
	}

	// simple testing
	// launch the app, then uninstall it
	public static void testingApp(AppInfoWrapper appInfo) {
		Env env = setUp(appInfo);
		AndroidDriver<AndroidElement> driver = env.driver();
		driver.closeApp();
		driver.removeApp(appInfo.getPkgName());
	}

	/**
	 * App testing is implemented as a function interface.
	 */
	public static void testingApp(AppInfoWrapper appInfo, BiConsumer<AppInfoWrapper, Env> testing) {
		Env env = setUp(appInfo);
		env.driver().resetApp();
		// Wait for app loading
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		testing.accept(appInfo, env);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		env.driver().closeApp();
	}

	/**
	 * Launch the app with package name and launchable activity name
	 */
	public static void testingApp(String pkgName, String actName, BiConsumer<String, Env> testing) {
		Env env = setUp(pkgName, actName);
		env.driver().resetApp();
		testing.accept(pkgName, env);
	}

	// Replay the bug we have found
	public static void replay(AppInfoWrapper appInfo, DirectedPseudograph<TTGNode, TTGEdge> graph) {
		int before = TTGReductionHelper.getEvents(graph).size();
		List<Event> replayEvents = TTGReduction.reduce(graph, DijkstraShortestPathFinder.class, PathEventCollector.class);
//		List<Event> replayEvents = TTGReductionHelper.getEvents(graph);
		Queue<Event> replayEventQueue = TTGReductionHelper.getEventQueueForReplay(replayEvents);
		int after = replayEvents.size();
		System.out.println("# Events before reduction: " + before);
		System.out.println("# Events after reduction: " + after);
		System.out.println("Replay events:");
		replayEvents.forEach(System.out::println);
		if(replayEvents.isEmpty()) {
			System.out.println("# No bug found during testing.");
			return;
		}
		Timer timer = new Timer();
		timer.start();
		testingApp(appInfo, (info, env) -> {
			// Clean old logcat output
			Logcat.clean();
			while(! replayEventQueue.isEmpty()) {
				Event event = replayEventQueue.peek();
				assert event != null;
				try {
					System.out.println(event);
					event.injectEvent(appInfo, env);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				replayEventQueue.remove();
			}
		});
		timer.stop();
		System.out.println("# Finish replay.");
		System.out.println("# Time: " + timer.getDurationInSecond() + " s.");
		String log = Logcat.getLogAsString();
		if(Logcat.isException(log))
			System.out.println(log);
		else
			Log.println("No exception found.");
	}
}
