package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Common key event.
 * Press the key specified by the key code.
 * This event represents pressing down and releasing the key. 
 * 
 * This class is adapted from the class MonkeyKeyEvent of the Android Open Source Project.
 * 
 * @author yifei
 */
public class KeyEvent extends Event {
	public KeyEvent(int keyCode) {
		super(EVENT_TYPE_KEY);
		mKeyCode = keyCode;
	}
	
	public int getKeyCode() {
		return mKeyCode;
	}

	public int getAction() {
		return mAction;
	}

	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		driver.pressKeyCode(mKeyCode);
	}

	/**
	 * Touch actions are throttled via waitAction() method.
	 * Key events are throttled via inserting a ThrottleEvent manually after them. 
	 */
	@Override
	public boolean isThrottlable() {
		return true;
	}
	
	protected int mAction;
	protected int mKeyCode;
}
