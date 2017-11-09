package testing;

import java.util.ArrayList;
import java.util.List;

import testing.event.Throttle;
import util.AppPathResolver;
import util.Config;

/**
 * Process testing options.
 * 
 * @author yifei
 */
public class TestingOptions {
	private String emulatorName = null;

	private List<String> appPaths = null;
	
	private int numberOfEvents = 1000;

	public String getEmulatorName() {
		return emulatorName;
	}

	public List<String> getAppPaths() {
		return appPaths;
	}

	public int getnumberOfEvents() {
		return numberOfEvents;
	}
	
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
			else if(arg.equals("-app")) {
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
			// process throttle time
			else if(arg.equals("-throttle"))
				try {
					int throttle = Integer.valueOf(args[i + 1]);
					Throttle.v().init(throttle);
				} catch (Exception e) {
					System.out.println(args[i + 1] + " is not a valid throttle time. Use 100ms.");
				}
			// process the number of test cases
			else if(arg.equals("-event"))
				try {
					numberOfEvents = Integer.valueOf(args[i + 1]);
				} catch (Exception e) {
					System.out.println(args[i + 1] + " is not a valid integer. Inject 1000 events.");
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