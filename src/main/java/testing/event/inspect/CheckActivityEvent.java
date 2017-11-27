package testing.event.inspect;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.event.KeyEvent;
import testing.event.Throttle;
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
		String curAct = getCurrentActivity(env);
		Log.println("# Current activity: " + curAct);
		// If current is in current app, then save it to the current activity trace
		if(info.contains(curAct)) {
			env.appendActivity(curAct);
		} else {
			Log.println("# Try to return from " + curAct);
			// Try to return to the app being tested
			for(int i = 0; i < 10; i++) {
				if(info.contains(getCurrentActivity(env)))
					break;
				new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
				new ThrottleEvent(Throttle.v().getThrottleDuration() * 2).injectEvent(info, env);
			}
			// If testing does not return to the app, we relaunch the app.
			if(! info.contains(getCurrentActivity(env))) {
				String firstAct = env.getFirstActivity();
				String lastAct = env.getLastActivity();
				if(firstAct != null && firstAct.equals(lastAct))
					// env.driver().launchApp();
					System.out.println("#### App stopped.");
				else {
					Log.println("# Warning: testing has been distracted from the app " + info.getPkgName());
					System.exit(0);
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
