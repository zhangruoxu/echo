package util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
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

	/**
	 * Obtain the buggy apps
	 */
	@Test 
	public void test9() {
		Config.init(null);
		Map<String, String> pkgName2AppPath = new HashMap<>();
		for(int i = 1; i <= 69; i++) {
			String appPath = AppPathResolver.resolveAppPath(Config.v().get(Config.APPDIR), i);
			if(appPath == null)
				continue;
			String[] temp = appPath.split(Pattern.quote(File.separator));
			String appName = temp[temp.length - 1];
			ManifestParser parser = new ManifestParser(appPath);
//			System.out.println(parser.getPackageName());
			pkgName2AppPath.put(parser.getPackageName(), appPath);
		}

		List<String> buggyApps = Arrays.asList("jp.gr.java_conf.hatalab.mnv", "com.bwx.bequick", "org.liberty.android.fantastischmemo", "net.fercanet.LNM", "ch.blinkenlights.battery", "com.zoffcc.applications.aagtl", "com.eleybourn.bookcatalogue", "net.sf.andbatdog.batterydog", "com.evancharlton.mileage", "a2dp.Vol", "caldwell.ben.bites", "com.morphoss.acal", "com.chmod0.manpages", "com.gluegadget.hndroid", "org.beide.bomber", "cri.sanity", "es.senselesssolutions.gpl.weightchart", "org.passwordmaker.android", "com.google.android.photostream", "org.jtb.alogcat", "com.hectorone.multismssender", "com.teleca.jamendo", "com.templaro.opsiz.aka", "hu.vsza.adsdroid", "i4nc4mp.myLock", "org.jessies.dalvikexplorer", "com.example.amazed", "com.addi", "org.dnaq.dialer2", "com.fsck.k9", "org.scoutant.blokish", "com.beust.android.translate");
		File destDir = new File("C:\\Users\\yifei\\Desktop\\buggy");
		try {
			for(String s : buggyApps) {
				String appPath = pkgName2AppPath.get(s);
				if(appPath == null)
					continue;
				FileUtils.copyFileToDirectory(new File(appPath), destDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
