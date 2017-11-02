package testing;

import java.io.File;
import java.util.function.BiConsumer;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import testing.event.Throttle;
import testing.event.ThrottleEvent;
import util.AppInfoWrapper;
import util.Config;
import util.ManifestParser;

public class MainTest {
	@Test
	public void test1() {
		Main.main(new String[] {"-emulator", "Nexus_5_API_19", "-app", "0"});
	}

	@Test
	public void test1_2() {
		Main.main(new String[] {"-emulator", "Nexus_5_API_19_2", "-app", "0"});
	}

	@Test
	public void test2() {
		Main.main(new String[] {"-emulator", "Nexus_5_API_19", "-app", "0", "1"});
	}

	@Test
	public void test3() {
		Main.main(new String[] { "-app", "0", "1", "-emulator", "Nexus_5_API_19"});
	}

	@Test
	public void test4() {
		Config.init(null);
		String[] args = new String[] {"-app", "0", "-emulator", "Nexus_5_API_19" };
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new)
		.forEach(i -> {
			Main.testingApp(i, (info, d) -> {
				d.closeApp();
				d.removeApp(info.getPkgName());
			});
		});
	}

	@Test
	public void test5() {
		Config.init(null);
		String[] args = new String[] {"-app", "0", "-emulator", "Nexus_5_API_19" };
		ManifestParser manifestParser = new ManifestParser("1");
		System.out.println("# pkg name: " + manifestParser.getPackageName());
		System.out.println("# activity name: " + manifestParser.getLaunchableActivity());
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new)
		.forEach(i -> {
			Main.testingApp(i, (info, d) -> {
				d.startActivity(new Activity("arity.calculator", "calculator.Calculator"));
				d.closeApp();
				d.removeApp(info.getPkgName());
			});
		});
	}

	/**
	 * Test return from unwanted app
	 * Press back key to return
	 */
	@Test
	public void test6() {
		initTesting("0", (i, d) -> {
			d.startActivity(new Activity("arity.calculator", "calculator.Calculator"));
			if(! i.contains(d.currentActivity()))
				d.pressKeyCode(AndroidKeyCode.BACK);
		});
	}

	/**
	 * Launch the app with package name and launchable activity name 
	 */
	@Test
	public void test7() {
		Main.testingApp("com.example.yzhan.startmode", ".MainActivity", (p, d) -> {
			Dimension dimension = d.manage().window().getSize();
			System.out.println("# Window height: " + dimension.height);
			System.out.println("# Window width: " + dimension.width);
			d.closeApp();
		});
	}

	/**
	 * Test click button with coordinate
	 */
	@Test
	public void test8() {
		Throttle.v().init(500);
		System.out.println("Event throttle " + Throttle.v().getDuration());
		Main.testingApp("com.android.gesture.builder", ".GestureBuilderActivity", (p, d) -> {
			Dimension dimension = d.manage().window().getSize();
			System.out.println("# Window height: " + dimension.height);
			System.out.println("# Window width: " + dimension.width); 
			// click the "Add gesture" button
			new TouchAction(d).tap(280,  1700).waitAction(Throttle.v().getDuration()).perform();
			//  swipe
			new TouchAction(d).press(518, 518).moveTo(200, 200).release().waitAction(Throttle.v().getDuration()).perform();
			new TouchAction(d).press(520, 963).moveTo(-100, -100).release().waitAction(Throttle.v().getDuration()).perform();
			// multi couch
			TouchAction actOne = new TouchAction(d).press(357, 539).moveTo(-100, -100).waitAction(Throttle.v().getDuration()).release();
			TouchAction actTwo = new TouchAction(d).press(470, 583).moveTo(100, 100).waitAction(Throttle.v().getDuration()).release();
			new MultiTouchAction(d).add(actOne).add(actTwo).perform();
			d.closeApp();
		});
	}

	/**
	 * Test obtain screenshot and save to a file in current directory
	 */
	@Test
	public void test9() {
		initTesting("0", (i, d) -> {
			try {
				File screenshot = d.getScreenshotAs(OutputType.FILE);
				File dest = new File(String.join(File.separator, "." , screenshot.getName()));
				FileUtils.copyFile(screenshot, dest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Test obtaining window hierarchy of current screen
	 * The string is a xml file.
	 */
	@Test
	public void test10() {
		initTesting("0", (i, d) -> {
			System.out.println(d.getPageSource());
			d.closeApp();
		});
	}

	/**
	 * Test throttle time.
	 */
	@Test
	public void test11() {
		Config.init(null);
		String[] args = new String[] {"-app", "0", "-emulator", "Nexus_5_API_19", "-throttle", "500"};
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(i -> Main.testingApp(i, (info, d) -> {
			new ThrottleEvent();
			d.closeApp();
		}));
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
