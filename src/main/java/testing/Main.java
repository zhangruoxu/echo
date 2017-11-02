package testing;

import java.net.URL;
import java.util.function.BiConsumer;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
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
	private static AndroidDriver<AndroidElement> setUp(AppInfoWrapper appInfo) {
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
		return driver;
	}
	
	/**
	 * Setup the testing with package name and launchable activity name
	 */
	private static AndroidDriver<AndroidElement> setUp(String pkgName, String actName) {
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
		return driver;
	}
	
	// simple testing
	// launch the app, then uninstall it
	public static void testingApp(AppInfoWrapper appInfo) {
		AndroidDriver<AndroidElement> driver = setUp(appInfo);
		driver.closeApp();
		driver.removeApp(appInfo.getPkgName());
	}
	
	/**
	 * App testing is implemented as a function interface.
	 */
	public static void testingApp(AppInfoWrapper appInfo, BiConsumer<AppInfoWrapper, AndroidDriver<AndroidElement>> testing) {
		AndroidDriver<AndroidElement> driver = setUp(appInfo);
		testing.accept(appInfo, driver);
	}
	
	/**
	 * Launch the app with package name and launchable activity name
	 */
	public static void testingApp(String pkgName, String actName, BiConsumer<String, AndroidDriver<AndroidElement>> testing) {
		AndroidDriver<AndroidElement> driver = setUp(pkgName, actName);
		testing.accept(pkgName, driver);
	}
}
