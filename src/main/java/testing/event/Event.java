package testing.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * An abstract class for event. 
 * 
 * @author yifei
 */
public abstract class Event {
	protected int eventType;
    public static final int EVENT_TYPE_KEY = 0;
    public static final int EVENT_TYPE_TOUCH = 1;
    public static final int EVENT_TYPE_TRACKBALL = 2;
    public static final int EVENT_TYPE_ROTATION = 3;  // Screen rotation
    public static final int EVENT_TYPE_ACTIVITY = 4;
    public static final int EVENT_TYPE_FLIP = 5; // Keyboard flip
    public static final int EVENT_TYPE_THROTTLE = 6;
    public static final int EVENT_TYPE_PERMISSION = 7;
    public static final int EVENT_TYPE_NOOP = 8;

    public static final int INJECT_SUCCESS = 1;
    public static final int INJECT_FAIL = 0;

    // error code for remote exception during injection
    public static final int INJECT_ERROR_REMOTE_EXCEPTION = -1;
    // error code for security exception during injection
    public static final int INJECT_ERROR_SECURITY_EXCEPTION = -2;

    public Event(int type) {
        eventType = type;
    }

    public int getEventType() {
        return eventType;
    }
	
    // Perform the corresponding event 
	public abstract void injectEvent(AndroidDriver<AndroidElement> driver);
	
    /**
     * @return true if it is safe to throttle after this event, and false otherwise.
     */
    public boolean isThrottlable() {
        return true;
    }
}
