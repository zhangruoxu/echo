package testing.event;

import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import util.PointF;

/**
 * This class represents multitouch event, like zooming and pinching.
 * @author yifei
 */
public class MultiTouchEvent extends MotionEvent {

	public MultiTouchEvent(long downAt, int metaState) {
		super(Event.EVENT_TYPE_TOUCH, downAt, metaState);
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		assert mFromPointers.size() == mMoves.size();
		MultiTouchAction action = new MultiTouchAction(driver);
		for(int i = 0; i < mFromPointers.size(); i++) {
			PointF from = mFromPointers.get(i);
			PointF to = mMoves.get(i);
			int fromX = Math.round(from.x);
			int fromY = Math.round(from.y);
			int toX = Math.round(to.x);
			int toY = Math.round(to.y);
			action.add(new TouchAction(driver).press(fromX, fromY).moveTo(toX - fromX, toY - fromY).release().waitAction(Throttle.v().getDuration()));
		}
		action.perform();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Multitouch from [");
		for(int i = 0; i < mFromPointers.size(); i++) {
			PointF p = mFromPointers.get(i);
			builder.append("(").append(p.x).append(", ").append(p.y).append("), ");
		}
		builder.append("], to[");
		for(int i = 0; i < mMoves.size(); i++) {
			PointF p = mMoves.get(i);
			builder.append("(").append(p.x).append(", ").append(p.y).append("), ");
		}
		builder.append("].");
		return builder.toString();
	}
}
