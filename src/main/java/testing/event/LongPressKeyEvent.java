package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Long press key event.
 * Long press the key specified by the key code.
 * 
 * This event represents pressing down, holding and releasing the key.
 * 
 * @author yifei
 */
public class LongPressKeyEvent extends KeyEvent {
	public LongPressKeyEvent(int action, int keyCode) {
		super(action, keyCode);
	}
	
	@Override
	public void injectEvent(AndroidDriver<AndroidElement> driver) {
		driver.longPressKeyCode(mKeyCode);
	}
}
