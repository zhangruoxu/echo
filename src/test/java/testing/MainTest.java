package testing;

import org.junit.Test;

import util.AppInfoWrapper;
import util.Config;

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
}
