package testing.random;

import java.util.function.BiConsumer;

import org.junit.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testing.Main;
import testing.TestingOptions;
import testing.event.Event;
import testing.event.Throttle;
import util.AppInfoWrapper;
import util.Config;

public class TestRandomEventSource {
	@Test
	public void test1() {
		initTesting("0", (info, d) -> {
			Throttle.v().init(500);
			RandomEventSource eventSource = new RandomEventSource(d, 0);
			eventSource.adjustEventFactors();
			for(int i = 0; i < 5000; i++) {
				Event e = eventSource.getNextEvent();
				System.out.println(e);
				e.injectEvent(d);
			}
		});
	}
	
	/**
	 * Initialize Appium testing
	 */
	private void initTesting(String id, BiConsumer<AppInfoWrapper, AndroidDriver<AndroidElement>> testing) {
		Config.init(null);
		String[] args = new String[] {"-app", id, "-emulator", "Nexus_5_API_19"};
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(i -> Main.testingApp(i, testing));
	}
}
