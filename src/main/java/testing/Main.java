package testing;

import java.net.URL;
import java.util.List;
import java.util.function.BiConsumer;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import util.Config;
import util.ManifestParser;

/**
 * @author yifei
 * The main class for Appium testing
 */

public class Main {
	private static final String APPIUM_URL = "http://0.0.0.0:4723/wd/hub";
	
	public static void main(String[] args) {
		Config.init(null);
		TestingOptions.v().processOptions(args);
		List<String> appPaths = TestingOptions.v().getAppPaths();
		appPaths.forEach(Main::testingApp);
	}

	// simple testing
	// launch the app, then uninstall it
	public static void testingApp(String appPath) {
		String pkgName = new ManifestParser(appPath).getPackageName();
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.APP, appPath);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		AppiumDriver<MobileElement> driver = null;
		try {
			driver = new AppiumDriver<>(new URL(APPIUM_URL), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		driver.closeApp();
		driver.removeApp(pkgName);
	}
	
	/**
	 * App testing is implemented as a function interface.
	 */
	public static void testingApp(String appPath, BiConsumer<String, AppiumDriver<MobileElement>> testing) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.APP, appPath);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		AppiumDriver<MobileElement> driver = null;
		try {
			driver = new AppiumDriver<>(new URL(APPIUM_URL), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		testing.accept(appPath, driver);
	}
}
