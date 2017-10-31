package testing;

import java.util.List;

import org.junit.Test;

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
		List<String> appPaths = TestingOptions.v().getAppPaths();
		for(String appPath : appPaths)
			Main.testingApp(appPath, (s, d) -> {
				String pkgName = ManifestParser.getPackageName(appPath);
				d.closeApp();
				d.removeApp(pkgName);
			});
	}
}
