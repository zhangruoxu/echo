package testing.event;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.PointF;

/**
 * This class represents drag event. 
 * @author yifei
 */
public class DragEvent extends MotionEvent {
	public DragEvent(long downAt, int metaState) {
		super(Event.EVENT_TYPE_TOUCH, downAt, metaState);
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		assert mFromPointers.size() == 1;
		assert mMoves.size() == 1;
		PointF from = mFromPointers.get(0);
		PointF to = mMoves.get(0);
		int fromX = Math.round(from.x);
		int fromY = Math.round(from.y);
		int toX = Math.round(to.x);
		int toY = Math.round(to.y);
		new MultiTouchAction(driver)
		.add(new TouchAction(driver).press(fromX, fromY).moveTo(toX, toY).release()
		.waitAction(Throttle.v().getDuration())).perform();
	}

	@Override
	public String toString() {
		assert mFromPointers.size() == 1;
		assert mMoves.size() == 1;
		PointF from = mFromPointers.get(0);
		PointF to = mMoves.get(0);
		int fromX = Math.round(from.x);
		int fromY = Math.round(from.y);
		int toX = Math.round(to.x);
		int toY = Math.round(to.y);
		StringBuilder builder = new StringBuilder();
		builder.append("[DragEvent] Drag from (").append(fromX).append(", ").append(fromY).append(") to (")
		.append(toX).append(", ").append(toY).append(").");
		return builder.toString();
	}
}
