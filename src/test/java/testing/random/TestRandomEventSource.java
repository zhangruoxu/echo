package testing.random;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.Main;
import testing.TestingOptions;
import testing.event.DragEvent;
import testing.event.Event;
import testing.event.KeyEvent;
import testing.event.TapEvent;
import testing.event.Throttle;
import testing.event.ThrottleEvent;
import testing.event.inspect.CheckActivityEvent;
import testing.event.inspect.InspectEvent;
import testing.event.inspect.ScreenshotEvent;
import util.Config;
import util.Log;
import util.PointF;
import util.Timer;

public class TestRandomEventSource {
	@Test
	public void test1() {
		Timer timer = new Timer();
		timer.start();
		initTesting("0", (info, env) -> {
			Throttle.v().init(500);
			RandomEventSource eventSource = new RandomEventSource(null, env, 10000);
			eventSource.adjustEventFactors();
			List<Event> failedEvents = new ArrayList<>();
			for(int i = 0; i < 10000; i++) {
				System.out.println("# Event " + i);
				Event event = eventSource.getNextEvent();
				System.out.println(event);
				try {
					event.injectEvent(info, env);
				} catch (Exception e) {
					e.printStackTrace();
					failedEvents.add(event);
				}
			}
			System.out.println("# Failed events: " + failedEvents.size());
			failedEvents.forEach(System.out::println);
		});
		timer.stop();
		System.out.println("# Time: " + timer.getDurationInSecond() + "s.");
	}

	/**
	 * Test end call key. This key makes the device sleep.
	 */
	@Test
	public void test2() {
		initTesting("0", (info, d) -> {
			new KeyEvent(AndroidKeyCode.KEYCODE_ENDCALL).injectEvent(info, d);
		});
	}

	/**
	 * Test menu and home key.
	 */
	@Test
	public void test3() {
		Timer timer = new Timer();
		timer.start();
		initTesting("0", (info, d) -> {
			new KeyEvent(AndroidKeyCode.MENU).injectEvent(info, d);
			new KeyEvent(AndroidKeyCode.HOME).injectEvent(info, d);
		});
		timer.stop();
		System.out.println("# Time: " + timer.getDurationInSecond() + "s.");
	}

	/**
	 * Test tap event
	 */
	@Test
	public void test4() {
		initTesting("0", (info, env) -> {
			AndroidDriver<AndroidElement> d = env.driver();
			System.out.println("# Height: " + d.manage().window().getSize().getHeight());
			System.out.println("# Width: " + d.manage().window().getSize().getWidth());
			new TapEvent().addFrom(0, new PointF(1, 1775)).injectEvent(info, env);
		});
	}

	/**
	 * Test drag event
	 */
	@Test
	public void test5() {
		initTesting("0", (info, d) -> {
			new DragEvent().addFromTo(0, new PointF(398, 1388),  new PointF(365, 1371)).injectEvent(info, d);
		});
	}

	/**
	 * Test try to return previous app
	 * CheckActivityEvent issues a BACK key pressing event, which may drive the app to the app being tested
	 */
	@Test
	public void test6() {
		initTesting("0", (info, env) -> {
			Throttle.v().init(1000);
			new KeyEvent(AndroidKeyCode.KEYCODE_CONTACTS).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			new CheckActivityEvent().injectEvent(info, env);
		});
	}

	/**
	 * Test the test case trace.
	 */
	@Test
	public void test7() {
		Timer timer = new Timer();
		timer.start();
		initTesting("0", (info, env) -> {
			Throttle.v().init(500);
			RandomEventSource eventSource = new RandomEventSource(null, env, 10000);
			eventSource.adjustEventFactors();
			final int eventCount = 100;
			int eventCounter = 0;
			List<Event> failedEvents = new ArrayList<>();
			while(eventCounter < eventCount) {
				System.out.println("# Event " + eventCounter);
				Event event = eventSource.getNextEvent();
				System.out.println(event);
				try {
					event.injectEvent(info, env);
					if(!( event instanceof ThrottleEvent || event instanceof InspectEvent))
						eventCounter++;
				} catch (Exception e) {
					e.printStackTrace();
					failedEvents.add(event);
				}
			}
			System.out.println("# Failed events: " + failedEvents.size());
			failedEvents.forEach(System.out::println);
			Deque<Event> eventTraces = env.getEventTrace();
			System.out.println("## Event traces: " + eventTraces.size());
			System.out.println("## ThrottleEvent: " + eventTraces.stream().filter(ThrottleEvent.class::isInstance).count());
		});
		timer.stop();
		System.out.println("# Time: " + timer.getDurationInSecond() + "s.");
	}

	/**
	 * Test runTestingCycles().
	 */
	@Test
	public void test8() {
		Timer timer = new Timer();
		timer.start();
		initTesting("0", (info, env) -> {
			Throttle.v().init(500);
			TestingOptions.v().setNumberOfEvents(100);
			RandomEventSource eventSource = new RandomEventSource(info, env, 10000);
			eventSource.runTestingCycles();
			Deque<Event> eventTraces = env.getEventTrace();
			System.out.println("## Event traces: " + eventTraces.size());
			System.out.println("## ThrottleEvent: " + eventTraces.stream().filter(ThrottleEvent.class::isInstance).count());
		});
		timer.stop();
		System.out.println("# Time: " + timer.getDurationInSecond() + "s.");
	}

	/**
	 * Test screenshot event
	 */
	@Test
	public void test9() {
		initTesting("0", (info, env) -> {
			new ScreenshotEvent().injectEvent(info, env);
		});
	}

	/**
	 * Test the real-world apps.
	 */
	@Test
	public void test10() {
		for(int i = 1; i <= 1; i++) {
			Timer timer = new Timer();
			timer.start();
			initTesting(Integer.toString(i), (info, env) -> {
				TestingOptions.v().setNumberOfEvents(5000);
				String output = Config.v().get(Config.OUTPUT);
				File outputDir = new File(output);
				if(! outputDir.exists())
					outputDir.mkdir();
				String fileName = String.join(File.separator, output, info.getAppFileName() + ".txt");
				try(PrintStream printStream = new PrintStream(fileName)) {
					Log.init(printStream);
					RandomEventSource eventSource = new RandomEventSource(info, env, 8888);
					eventSource.runTestingCycles();
					timer.stop();
					Log.println("# Time: " + timer.getDurationInSecond() + " s.");
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			});
		}
	}

	/**
	 * Test keycode_escape
	 */
	@Test
	public void test11() {
		initTesting("1", (info, env) -> {
			Throttle.v().init(1000);
			new TapEvent().addFrom(0, new PointF(522, 836)).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			new KeyEvent(AndroidKeyCode.KEYCODE_BUTTON_B).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			new CheckActivityEvent().injectEvent(info, env);
		});
	}
	
	/**
	 * Test keycode_escape
	 */
	@Test
	public void test12() {
		initTesting("0", (info, env) -> {
			Throttle.v().init(1000);
			new ThrottleEvent().injectEvent(info, env);
			new KeyEvent(AndroidKeyCode.KEYCODE_BUTTON_B).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			new CheckActivityEvent().injectEvent(info, env);
		});
	}
	
	/**
	 * Initialize Appium testing
	 */
	private void initTesting(String id, BiConsumer<AppInfoWrapper, Env> testing) {
		Config.init(null);
		String[] args = new String[] {"-app", id, "-emulator", "Nexus_5_API_19"};
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(i -> Main.testingApp(i, testing));
	}
}
