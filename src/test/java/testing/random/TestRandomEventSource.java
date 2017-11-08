package testing.random;

import java.util.ArrayList;
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
import util.Config;
import util.PointF;

public class TestRandomEventSource {
	@Test
	public void test1() {
		initTesting("0", (info, env) -> {
			Throttle.v().init(500);
			RandomEventSource eventSource = new RandomEventSource(null, env, 10000);
			eventSource.adjustEventFactors();
			List<Event> failedEvents = new ArrayList<>();
			for(int i = 0; i < 5000; i++) {
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
		initTesting("0", (info, d) -> {
			new KeyEvent(AndroidKeyCode.MENU).injectEvent(info, d);
			new KeyEvent(AndroidKeyCode.HOME).injectEvent(info, d);
		});
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
			new TapEvent(-1, -1).addFrom(0, new PointF(1, 1775)).injectEvent(info, env);
		});
	}

	/**
	 * Test drag event
	 */
	@Test
	public void test5() {
		initTesting("0", (info, d) -> {
			new DragEvent(-1, -1).addFromTo(0, new PointF(398, 1388),  new PointF(365, 1371)).injectEvent(info, d);
		});
	}

	/**
	 * Test relaunch the testing app during testing
	 * 
	 * The app will be relaunched, rather than be resumed.
	 */
	@Test
	public void test6() {
		initTesting("0", (info, d) -> {
			//			Throttle.v().init(500);
			//			new KeyEvent(AndroidKeyCode.KEYCODE_DPAD_CENTER).injectEvent(info, d);
			//			new ThrottleEvent().injectEvent(info, d);
			//			new CheckActivityEvent().injectEvent(info, d);
			//			new ThrottleEvent().injectEvent(info, d);
			//			new KeyEvent(AndroidKeyCode.KEYCODE_CONTACTS).injectEvent(info, d);
			//			new ThrottleEvent().injectEvent(info, d);
			//			new CheckActivityEvent().injectEvent(info, d);
			//			new ThrottleEvent().injectEvent(info, d);

			Throttle.v().init(1000);
			new KeyEvent(AndroidKeyCode.KEYCODE_DPAD_CENTER).injectEvent(info, d);
			new ThrottleEvent().injectEvent(info, d);
			new KeyEvent(AndroidKeyCode.KEYCODE_HOME).injectEvent(info, d);
			new ThrottleEvent().injectEvent(info, d);
			new TapEvent(-1, -1).addFrom(0, new PointF(900, 1600)).injectEvent(info, d);
			new ThrottleEvent().injectEvent(info, d);
			new CheckActivityEvent().injectEvent(info, d);
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
