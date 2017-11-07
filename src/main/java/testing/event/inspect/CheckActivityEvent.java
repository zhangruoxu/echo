package testing.event.inspect;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testing.event.Event;
import testing.event.ThrottleEvent;
import util.AppInfoWrapper;

/**
 * This event is used to inspect the current activity
 */
public class CheckActivityEvent extends Event {
	public CheckActivityEvent() {
		super(-1);
	}

	@Override
	public void injectEvent(AppInfoWrapper info, AndroidDriver<AndroidElement> driver) {
		String curAct =  expandClassName(info, driver.currentActivity());
		System.out.println("# Current activity: " + curAct);
		if(! info.contains(curAct)) {
			driver.launchApp();
			new ThrottleEvent().injectEvent(info, driver);
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
