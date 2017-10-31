package testing;

import java.util.ArrayList;
import java.util.List;

import util.AppPathResolver;
import util.Config;

/**
 * Process testing options.
 * 
 * @author yifei
 */
public class TestingOptions {
	private String emulatorName = null;
	public String getEmulatorName() {
		return emulatorName;
	}

	public List<String> getAppPaths() {
		return appPaths;
	}

	private List<String> appPaths = null;
		
	private TestingOptions() {}
	
	public static final TestingOptions v() {
		return SingletonHolder.singleton;
	}
	
	public void processOptions(String[] args) {
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			// process emulator name
			if(arg.equals("-emulator"))
				emulatorName = args[i + 1];
			// process app paths
			if(arg.equals("-app")) {
				List<Integer> appIds = new ArrayList<>();
				for(int j = i + 1; j < args.length; j++) {
					try {
						appIds.add(Integer.valueOf(args[j]));
					} catch (Exception e) {
						break;
					}
				}
				appPaths = AppPathResolver.resolveAppPaths(Config.v().get(Config.APPDIR), appIds);
			}
		}
		assert emulatorName != null;
		assert appPaths != null && ! appPaths.isEmpty();
	}
	
	/**
	 * Multithread-safe singleton holder.
	 */
	private static class SingletonHolder {
		private static final TestingOptions singleton = new TestingOptions();
	}
}