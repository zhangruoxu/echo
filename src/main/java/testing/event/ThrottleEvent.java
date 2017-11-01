package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.Log;

/**
 * Inject a fixed time delay between events during testing to let GUI respond to the injected events.
 * 
 * @author yifei
 */

public class ThrottleEvent extends Event {
	public ThrottleEvent(long throttle) {
		super(Event.EVENT_TYPE_THROTTLE);
		this.mthrottle =throttle;
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		try {
			Thread.sleep(mthrottle);
		} catch (Exception e) {
			e.printStackTrace();
			Log.println("Inject " + this.getClass().getName() + " failed.");
		}
	}
	
	private long mthrottle;
}
