package testing.event.inspect;

import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Env;
import testing.event.Event;
import testing.event.KeyEvent;
import testing.event.ThrottleEvent;

/**
 * This event is used to inspect the current activity
 */
public class CheckActivityEvent extends Event {
	public CheckActivityEvent() {
		super(Event.EVENT_INSPECT);
	}

	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curAct = expandClassName(info, env.driver().currentActivity());
		System.out.println("# Current activity: " + curAct);
		if(! info.contains(curAct)) {
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
