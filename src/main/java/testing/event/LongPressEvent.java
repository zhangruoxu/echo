package testing.event;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.PointF;

/**
 * This class represents long-pressing a coordinate on the touchscreen.
 * 
 * @author yifei
 */
public class LongPressEvent extends MotionEvent {

	public LongPressEvent(long downAt, int metaState) {
		super(Event.EVENT_TYPE_TOUCH, downAt, metaState);
		assert mFromPointers.size() == 1;
		PointF p = mFromPointers.get(0);
		mX = Math.round(p.x);
		mY = Math.round(p.y);
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		new TouchAction(driver).longPress(mX, mY).release().waitAction(Throttle.v().getDuration()).perform();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[LongPressEvent] Long press (").append(mX).append(", ").append(mY).append(").s");
		return builder.toString();
	}
	
	private int mX;
	private int mY;
}
