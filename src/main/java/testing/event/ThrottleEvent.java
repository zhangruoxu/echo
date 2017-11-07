package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.AppInfoWrapper;
import util.Log;

/**
 * Inject a fixed time delay between events during testing to let GUI respond to the injected events.
 * 
 * This class is adapted from the MonkeyThrottleEvent of the Android Open Source Project.
 * 
 * @author yifei
 */

public class ThrottleEvent extends Event {
	public ThrottleEvent() {
		super(Event.EVENT_TYPE_THROTTLE);
		this.mthrottle = Throttle.v().getThrottleDuration();
	}

	@Override
	public void injectEvent(AppInfoWrapper info, AndroidDriver<AndroidElement> driver) {
		try {
			Thread.sleep(mthrottle);
		} catch (Exception e) {
			e.printStackTrace();
			Log.println("Inject " + this.getClass().getName() + " failed.");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[ThrottleEvent] A fixed time delay: ").append(mthrottle);
		return builder.toString();
	}
	private long mthrottle;
}
