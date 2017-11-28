package testing;

import java.net.URL;
import java.util.function.BiConsumer;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
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
			// TODO: handle exception
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
