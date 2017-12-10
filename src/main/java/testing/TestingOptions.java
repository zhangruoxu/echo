package testing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import testing.event.Throttle;
import util.AppPathResolver;
import util.Config;
import util.Log;

/**
 * Process testing options.
 * 
 * @author yifei
 */
public class TestingOptions {
	private String emulatorName = "";

	private List<String> appPaths = null;

	private int throttle = 500;
	
	private int numberOfEvents = 5000;
	
	private int seed = 0;
	
	private boolean replay = false;

	public String getEmulatorName() {
		return emulatorName;
	}

	public List<String> getAppPaths() {
		return appPaths;
	}
	
	public int getThrottle() {
		return throttle;
	}
	
	public int getNumberOfEvents() {
		return numberOfEvents;
	}

	public void setNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
	}
	
	public int getSeed() {
		return seed;
	}
	
	public boolean isReplay() {
		return replay;
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
					throttle = Integer.valueOf(args[i + 1]);
					Throttle.v().init(throttle);
				} catch (Exception e) {
					Log.println(args[i + 1] + " is not a valid throttle time. Use 500 ms.");
				}
			// process the number of test cases
			else if(arg.equals("-event"))
				try {
					numberOfEvents = Integer.valueOf(args[i + 1]);
				} catch (Exception e) {
					Log.println(args[i + 1] + " is not a valid integer. Inject 5000 events.");
				}
			// process the seed
			else if(arg.equals("-seed"))
				try {
					seed = Integer.valueOf(args[i + 1]);
				} catch (Exception e) {
					Log.println(args[i + 1] + " is not a valid integer. Use 0 as seed.");
				}
			// whether replay the error
			else if(arg.equals("-replay"))
				replay = true;
		}
		// assert emulatorName != null;
		assert appPaths != null && ! appPaths.isEmpty();
	}

	// Dump all the options
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		try {
			Field flds[] = TestingOptions.class.getDeclaredFields();
			for(Field fld : flds) {
				buffer.append(fld.getName());
				buffer.append(": ");
				buffer.append(fld.get(this).toString());
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	/**
	 * Multithread-safe singleton holder.
	 */
	private static class SingletonHolder {
		private static final TestingOptions singleton = new TestingOptions();
	}
}