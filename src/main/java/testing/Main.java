package testing;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.function.BiConsumer;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import testing.random.RandomEventSource;
import testing.ttg.TestingTraceGraph;
import testing.ttg.node.TTGNodeFactory;
import util.Config;
import util.Log;
import util.Timer;

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
		System.out.println(TestingOptions.v().toString());
		// Test one app for each time
		AppInfoWrapper appInfo = new AppInfoWrapper(TestingOptions.v().getAppPaths().get(0));
		Timer timer = new Timer();
		timer.start();
		testingApp(appInfo, (info, env) -> {
			Logcat.clean();
			String output = Config.v().get(Config.OUTPUT);
			File outputDir = new File(output);
			if(! outputDir.exists())
				outputDir.mkdir();
			String fileName = String.join(File.separator, output, info.getAppFileName() + ".txt");
			try(PrintStream printStream = new PrintStream(fileName)) {
				Log.init(printStream);
				// Run random testing
				RandomEventSource eventSource = new RandomEventSource(info, env, TestingOptions.v().getSeed());
				eventSource.runTestingCycles();
				timer.stop();
				Log.println("# Time: " + timer.getDurationInSecond() + " s.");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		});
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
		TTGNodeFactory.reset();
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
	}

	/**
	 * Launch the app with package name and launchable activity name
	 */
	public static void testingApp(String pkgName, String actName, BiConsumer<String, Env> testing) {
		Env env = setUp(pkgName, actName);
		env.driver().resetApp();
		testing.accept(pkgName, env);
	}
}
