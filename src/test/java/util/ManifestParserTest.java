package util;

import org.junit.Test;

public class ManifestParserTest {

	@Test
	public void test1() {
		Config.init(null);
		ManifestParser parser = new ManifestParser(AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), 0));
		parser.getActivityNames().forEach(System.out::println);
	}

	@Test
	public void test2() {
		Config.init(null);
		ManifestParser parser = new ManifestParser(AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), 1));
		parser.getActivityNames().forEach(System.out::println);
	}

	/*@Test
	public void test3() {
		ManifestParser.main(new String[] {"1", "2", "3"});
	}

	@Test
	public void test4() {
		ManifestParser.main(new String[] {"all"});
	}
*/
	@Test
	public void test5() {
		Config.init(null);
		ManifestParser parser = new ManifestParser(AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), 9));
		parser.getActivityNames().forEach(System.out::println);
	}

	@Test
	public void test6() {
		Config.init(null);
		for(int i = 0; i < 67; i++) {
			System.out.println("# " + i);
			String appPath = AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), i);
			if(appPath == null)
				continue;
			ManifestParser parser = new ManifestParser(appPath);
			parser.getLaunchableActivities().forEach(System.out::println);
		}
	}
	
	@Test
	public void test7() {
		Config.init(null);
		for(int i = 0; i < 67; i++) {
			System.out.println("# " + i);
			String appPath = AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), i);
			if(appPath == null)
				continue;
			ManifestParser parser = new ManifestParser(appPath);
			System.out.println(parser.getLaunchableActivity());
		}
	}
	
	@Test
	public void test8() {
		Config.init(null);
		System.out.println("# " + 48);
		String appPath = AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), 48);
		if(appPath == null)
			return;
		ManifestParser parser = new ManifestParser(appPath);
		parser.getActivityNames().forEach(System.out::println);
		parser.getLaunchableActivities().forEach(System.out::println);
	}
}
