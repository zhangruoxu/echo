package testing;

import java.net.URL;
import java.util.function.BiConsumer;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import util.AppInfoWrapper;
import util.Config;

/**
 * @author yifei
 * The main class for Appium testing
 */

public class Main {
	private static final String APPIUM_URL = "http://0.0.0.0:4723/wd/hub";
	
	public static void main(String[] args) {
		Config.init(null);
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(Main::testingApp);
	}

	/**
	 * Setup testing session
	 */
	private static AppiumDriver<MobileElement> setUp(AppInfoWrapper appInfo) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appInfo.getPkgName());
		capabilities.setCapability(MobileCapabilityType.APP, appInfo.getAppPath());
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		AppiumDriver<MobileElement> driver = null;
		try {
			driver = new AppiumDriver<>(new URL(APPIUM_URL), capabilities);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return driver;
	}
	
	// simple testing
	// launch the app, then uninstall it
	public static void testingApp(AppInfoWrapper appInfo) {
		AppiumDriver<MobileElement> driver = setUp(appInfo);
		driver.closeApp();
		driver.removeApp(appInfo.getPkgName());
	}
	
	/**
	 * App testing is implemented as a function interface.
	 */
	public static void testingApp(AppInfoWrapper appInfo, BiConsumer<AppInfoWrapper, AppiumDriver<MobileElement>> testing) {
		AppiumDriver<MobileElement> driver = setUp(appInfo);
		testing.accept(appInfo, driver);
	}
}
