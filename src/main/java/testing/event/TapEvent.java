package testing.event;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.AppInfoWrapper;
import util.PointF;

/**
 * This class represents touch events from the touchscreen.
 * 
 * This class is adapted from the class MonkeyTouchEvent of the Android Open Source Project.
 * 
 * @author yifei
 */
public class TapEvent extends MotionEvent {

	public TapEvent(long downAt, int metaState) {
		super(Event.EVENT_TYPE_TOUCH, downAt, metaState);
	}

	/**
	 * Inject a touch event. Touch a coordinate then waiting for a short period of time.
	 */
	@Override
	public void injectEvent(AppInfoWrapper info, AndroidDriver<AndroidElement> driver) {
		assert mFromPointers.size() == 1;
		PointF p = mFromPointers.get(0);
		int mX = Math.round(p.x);
		int mY = Math.round(p.y);
		new TouchAction(driver).tap(mX, mY).waitAction(Throttle.v().getDuration()).perform();
	}
	
	@Override
	public String toString() {
		assert mFromPointers.size() == 1;
		PointF p = mFromPointers.get(0);
		int mX = Math.round(p.x);
		int mY = Math.round(p.y);
		StringBuilder builder = new StringBuilder();
		builder.append("[TouchEvent] Touch the point (").append(mX).append(", ").append(mY).append(").");
		return builder.toString();
	}
}
