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
		assert mFromPointers.size() == 1;
		assert mMoves.size() == 1;
		
		PointF from = mFromPointers.get(0);
		PointF to = mMoves.get(0);
		fromX = Math.round(from.x);
		fromY = Math.round(from.y);
		toX = Math.round(to.x);
		toY = Math.round(to.y);
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		new MultiTouchAction(driver)
		.add(new TouchAction(driver).press(fromX, fromY).moveTo(toX, toY).release()
		.waitAction(Throttle.v().getDuration())).perform();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[DragEvent] Drag from (").append(fromX).append(", ").append(fromY).append(") to (")
		.append(toX).append(", ").append(toY).append(").");
		return builder.toString();
	}
	
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
}
