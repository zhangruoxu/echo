package testing.event.inspect;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.Logcat;
import testing.event.KeyEvent;
import testing.event.Throttle;
import testing.event.ThrottleEvent;
import testing.random.RandomEventSource;
import util.Log;

/**
 * This event is used to inspect the current activity
 * 
 * This event can detects whether the testing has been distracted from the app being tested.
 * If that happens, we check whether it is caused by the exceptions and crashes via inspecting logcat entries. 
 * If true, testing terminates. If not, we try to return to the app being tested. 
 * We firstly try to press back button to return. If the app does not return, then the app is restarted. 
 * 
 * The app is restarted via directly launching the first activity (activity traces are kept during testing), 
 * which does not clean the previous data generated during testing. 
 * 
 * @author yifei
 */
public class CheckActivityEvent extends InspectEvent {
	public CheckActivityEvent() {
		super();
	}

	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curAct = getCurrentActivity(env);
		Log.println("# Current activity: " + curAct);
		// If current is in current app, then save it to the current activity trace
		if(info.contains(curAct)) {
			env.appendActivity(env.driver().currentActivity());
			// Clean the logcat output
			Logcat.clean();
		} else {
			// Testing has quit from the app being tested
			// Obtain logcat to see whether there are exceptions
			if(Logcat.isException()) {
				// Error occurs
				// Pint the logcat info then stop testing
				Log.println("App error.");
				Log.println(Logcat.getLogAsString());
				RandomEventSource.notifyError();
			} else {
				// No error happens. 
				// Try to return to the app being tested
				for(int i = 0; i < 5; i++) {
					if(info.contains(getCurrentActivity(env)))
						break;
					new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
					new ThrottleEvent(Throttle.v().getThrottleDuration() * 2).injectEvent(info, env);
				}
				// If testing does not return to the app, we relaunch the app.
				if(! info.contains(getCurrentActivity(env))) {
					String firstAct = env.getFirstActivity();
					String lastAct = env.getLastActivity();
					if(firstAct != null && firstAct.equals(lastAct)) {
						// Start the first activity during testing.
						// Using the launching app API all the app data are lost.
						// The app is relaunched via starting the first activity in the testing trace.
						Activity mainActivity = new Activity(info.getPkgName(), firstAct);
						env.driver().startActivity(mainActivity);
					}
					else {
						Log.println("# Warning: testing has been distracted from the app " + info.getPkgName());
						System.exit(0);
					}
				}
			}
		}
	}

	/**
	 * return the class name of current activity
	 */
	private String getCurrentActivity(Env env) {
		AndroidDriver<AndroidElement> driver = env.driver();
		assert driver.getCurrentPackage() != null;
		assert driver.currentActivity() != null;
		return driver.getCurrentPackage() + driver.currentActivity();
	}

	@Override
	public String toString() {
		return "[CheckActivityEvent].";
	}
}
