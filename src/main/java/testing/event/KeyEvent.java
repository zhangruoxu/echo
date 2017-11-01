package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Common key event.
 * Press the key specified by the key code.
 * This event represents pressing down and releasing the key. 
 * 
 * @author yifei
 */
public class KeyEvent extends Event {
	public KeyEvent(int action, int keyCode) {
		super(EVENT_TYPE_KEY);
		mAction = action;
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

	protected int mAction;
	protected int mKeyCode;
}
