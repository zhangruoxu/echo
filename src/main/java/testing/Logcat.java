package testing;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.logging.LogEntry;

import util.Config;
import util.ExecuteCommand;

/**
 * This class interacts with logcat.
 * @author yifei
 */
public class Logcat {
	// Initialize Logcat class.
	public static void init(AppInfoWrapper info, Env env) {
		Logcat.info = info;
		Logcat.env = env;
	}
	
	// Clean logcat output
	public static void clean() {
		String cmdDir = String.join(File.separator, Config.v().get(Config.SDK), "platform-tools");
		ExecuteCommand.exec(cmdDir, Config.v().get(Config.ADB), "logcat", "-c");
	}
	
	// obtain logcat entries
	public static List<LogEntry> getLog() {
		assert env != null;
		return env.driver().manage().logs().get("logcat").getAll();
	}
	
	// obtain logcat entries as a string
	public static String getLogAsString() {
		assert env != null;
		return String.join("\n", getLog().stream().map(LogEntry::toString).collect(Collectors.toList()));
	}
	
	private Logcat() {}
	private static AppInfoWrapper info;
	private static Env env;
}