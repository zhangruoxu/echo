package testing;

import java.util.function.BiConsumer;

import org.junit.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
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
	 * Initialize Appium testing
	 */
	private void initTesting(String id, BiConsumer<AppInfoWrapper, AndroidDriver<MobileElement>> testing) {
		Config.init(null);
		String[] args = new String[] {"-app", id, "-emulator", "Nexus_5_API_19"};
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(i -> Main.testingApp(i, testing));
	}
}
