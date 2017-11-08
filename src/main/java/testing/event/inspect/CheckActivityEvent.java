package testing.event.inspect;

import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.event.KeyEvent;
import testing.event.ThrottleEvent;

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
		System.out.println("# Current activity: " + curAct);
		if(! info.contains(curAct)) {
			System.out.println("# Go to activity " + curAct);
			// Try to return to the app being tested
			new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
			new ThrottleEvent().injectEvent(info, env);
			if(! info.contains(expandClassName(info, env.driver().currentActivity()))) {
				System.out.println("# Warning: testing has been distracted from the app " + info.getPkgName());
				System.exit(0);
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
