package testing;

import java.util.function.BiConsumer;

import util.Config;

/**
 * This class defines utility methods for testing.
 * 
 * @author yifei
 */
public class TestUtil {
	/**
	 * Initialize Appium testing
	 */
	public static void initTesting(String id, BiConsumer<AppInfoWrapper, Env> testing) {
		Config.init(null);
		String[] args = new String[] {"-app", id, "-emulator", "Nexus_5_API_19"};
		TestingOptions.v().processOptions(args);
		TestingOptions.v().getAppPaths().stream().map(AppInfoWrapper::new).forEach(i -> Main.testingApp(i, testing));
	}
}
