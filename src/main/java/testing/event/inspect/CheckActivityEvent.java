package testing.event.inspect;

import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.event.KeyEvent;
import testing.event.ThrottleEvent;
import util.Log;

/**
 * This event is used to inspect the current activity
 * 
 * @author yifei
 */
public class CheckActivityEvent extends InspectEvent {
	public CheckActivityEvent() {
		super();
	}

	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curAct = expandClassName(info, env.driver().currentActivity());
		Log.println("# Current activity: " + curAct);
		// If current is in current app, then save it to the current activity trace
		if(info.contains(curAct)) {
			env.appendActivity(curAct);
		} else {
			Log.println("# Go to activity " + curAct);
			// Try to return to the app being tested
			new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			if(! info.contains(expandClassName(info, env.driver().currentActivity()))) {
				// If previous activity is the same as the first activity,
				// It is possible that the app exits.
				// Then we relaunch the app.
				String firstAct = env.getFirstActivity();
				String lastAct = env.getLastActivity();
				if(firstAct != null && firstAct.equals(lastAct))
					env.driver().launchApp();
				else {
				Log.println("# Warning: testing has been distracted from the app " + info.getPkgName());
				System.exit(0);
				}
			}
		}
	}

	/**
	 * The activity name obtained during testing may be incomplete (with package eliminated),
	 * this method expands the class name. 
	 */
	private String expandClassName(AppInfoWrapper info, String className) {
		String packageName = info.getPkgName();
		if (className.startsWith("."))
			return packageName + className;
		else if (!className.contains("."))
			return packageName + "." + className;
		else
			return className;
	}
	
	@Override
	public String toString() {
		return "[CheckActivityEvent].";
	}
}
