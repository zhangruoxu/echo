package testing.random;

import java.security.SecureRandom;
import java.util.Random;

import org.openqa.selenium.Dimension;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import testing.event.Event;
import testing.event.EventQueue;
import testing.event.EventSource;
import testing.event.KeyEvent;
import testing.event.TapEvent;
import util.AndroidKeyCodeWrapper;
import util.Log;
import util.PointF;

/**
 * Generate random event source.
 * 
 * This class is adapted from the class MonkeySourceRandom of the Android Open Source Project.
 * 
 * @author yifei
 */
public class RandomEventSource implements EventSource {
	/** Key events that move around the UI. */
	private static final int[] NAV_KEYS = {
			AndroidKeyCode.KEYCODE_DPAD_UP, AndroidKeyCode.KEYCODE_DPAD_DOWN,
			AndroidKeyCode.KEYCODE_DPAD_LEFT, AndroidKeyCode.KEYCODE_DPAD_RIGHT,
	};

	/**
	 * Key events that perform major navigation options (so shouldn't be sent
	 * as much).
	 */
	private static final int[] MAJOR_NAV_KEYS = {
			AndroidKeyCode.KEYCODE_MENU, /*KeyEvent.KEYCODE_SOFT_RIGHT,*/
			AndroidKeyCode.KEYCODE_DPAD_CENTER,
	};

	/** Key events that perform system operations. */
	private static final int[] SYS_KEYS = {
			AndroidKeyCode.KEYCODE_HOME, AndroidKeyCode.KEYCODE_BACK,
			AndroidKeyCode.KEYCODE_CALL, AndroidKeyCode.KEYCODE_ENDCALL,
			AndroidKeyCode.KEYCODE_VOLUME_UP, AndroidKeyCode.KEYCODE_VOLUME_DOWN, AndroidKeyCode.KEYCODE_VOLUME_MUTE,
			AndroidKeyCode.KEYCODE_MUTE,
	};

	public static final int FACTOR_TOUCH        = 0;
	public static final int FACTOR_MOTION       = 1;
	public static final int FACTOR_PINCHZOOM    = 2;
	public static final int FACTOR_TRACKBALL    = 3;
	public static final int FACTOR_ROTATION     = 4;
	public static final int FACTOR_PERMISSION   = 5;
	public static final int FACTOR_NAV          = 6;
	public static final int FACTOR_MAJORNAV     = 7;
	public static final int FACTOR_SYSOPS       = 8;
	public static final int FACTOR_APPSWITCH    = 9;
	public static final int FACTOR_FLIP         = 10;
	public static final int FACTOR_ANYTHING     = 11;
	public static final int FACTORZ_COUNT       = 12;    // should be last+1

	private static final int GESTURE_TAP = 0;
	private static final int GESTURE_DRAG = 1;
	private static final int GESTURE_PINCH_OR_ZOOM = 2;
	/** percentages for each type of event.  These will be remapped to working
	 * values after we read any optional values.
	 **/
	private float[] mFactors = new float[FACTORZ_COUNT];
	private int mEventCount;  //total number of events generated so far
	private AndroidDriver<AndroidElement> mDriver;
	private EventQueue mQ;
	private Random mRandom;

	public RandomEventSource(AndroidDriver<AndroidElement> driver, long seed) {
		// default values for random distributions
		// note, these are straight percentages, to match user input (cmd line args)
		// but they will be converted to 0..1 values before the main loop runs.
		mFactors[FACTOR_TOUCH] = 15.0f;
		mFactors[FACTOR_MOTION] = 10.0f;
		mFactors[FACTOR_TRACKBALL] = 15.0f;
		// Adjust the values if we want to enable rotation by default.
		mFactors[FACTOR_ROTATION] = 0.0f;
		mFactors[FACTOR_NAV] = 25.0f;
		mFactors[FACTOR_MAJORNAV] = 15.0f;
		mFactors[FACTOR_SYSOPS] = 2.0f;
		mFactors[FACTOR_APPSWITCH] = 2.0f;
		mFactors[FACTOR_FLIP] = 1.0f;
		// disbale permission by default
		mFactors[FACTOR_PERMISSION] = 0.0f;
		mFactors[FACTOR_ANYTHING] = 13.0f;
		mFactors[FACTOR_PINCHZOOM] = 2.0f;

		mEventCount = 0;
		mDriver = driver;
		mRandom = new SecureRandom();
		mRandom.setSeed((seed == 0) ? -1 : seed);
		mQ = new EventQueue();
	}
	/**
	 * Adjust the percentages (after applying user values) and then normalize to a 0..1 scale.
	 */
	private boolean adjustEventFactors() {
		// go through all values and compute totals for user & default values
		float userSum = 0.0f;
		float defaultSum = 0.0f;
		int defaultCount = 0;
		for (int i = 0; i < FACTORZ_COUNT; ++i) {
			if (mFactors[i] <= 0.0f) {   // user values are zero or negative
				userSum -= mFactors[i];
			} else {
				defaultSum += mFactors[i];
				++defaultCount;
			}
		}

		// if the user request was > 100%, reject it
		if (userSum > 100.0f) {
			Log.println("** Event weights > 100%");
			return false;
		}

		// if the user specified all of the weights, then they need to be 100%
		if (defaultCount == 0 && (userSum < 99.9f || userSum > 100.1f)) {
			Log.println("** Event weights != 100%");
			return false;
		}

		// compute the adjustment necessary
		float defaultsTarget = (100.0f - userSum);
		float defaultsAdjustment = defaultsTarget / defaultSum;

		// fix all values, by adjusting defaults, or flipping user values back to >0
		for (int i = 0; i < FACTORZ_COUNT; ++i) {
			if (mFactors[i] <= 0.0f) {   // user values are zero or negative
				mFactors[i] = -mFactors[i];
			} else {
				mFactors[i] *= defaultsAdjustment;
			}
		}

		// if verbose, show factors
		Log.println("// Event percentages:");
		for (int i = 0; i < FACTORZ_COUNT; ++i) {
			Log.println("//   " + i + ": " + mFactors[i] + "%");
		}

		// finally, normalize and convert to running sum
		float sum = 0.0f;
		for (int i = 0; i < FACTORZ_COUNT; ++i) {
			sum += mFactors[i] / 100.0f;
			mFactors[i] = sum;
		}
		return true;
	}

	/**
	 * set the factors
	 *
	 * @param factors percentages for each type of event
	 */
	public void setFactors(float factors[]) {
		int c = FACTORZ_COUNT;
		if (factors.length < c) {
			c = factors.length;
		}
		for (int i = 0; i < c; i++)
			mFactors[i] = factors[i];
	}

	public void setFactors(int index, float v) {
		mFactors[index] = v;
	}

	/**
	 * Generates a random motion event. This method counts a down, move, and up as multiple events.
	 *
	 * TODO:  Test & fix the selectors when non-zero percentages
	 * TODO:  Longpress.
	 * TODO:  Fling.
	 * TODO:  Meta state
	 * TODO:  More useful than the random walk here would be to pick a single random direction
	 * and distance, and divvy it up into a random number of segments.  (This would serve to
	 * generate fling gestures, which are important).
	 *
	 * @param random Random number source for positioning
	 * @param motionEvent If false, touch/release.  If true, touch/move/release.
	 *
	 */
	private void generatePointerEvent(Random random, int gesture) {
		// Obtain screen size
		Dimension dimension = mDriver.manage().window().getSize();
		PointF point = randomPoint(random, dimension);
		long downAt = System.currentTimeMillis();

		if(gesture == GESTURE_TAP) {
			mQ.addLast(new TapEvent(downAt, 0).addPointer(0, point));
		} else if(gesture == GESTURE_DRAG) {
			
		}
	}

	// Generate a random coordinate in current screen
	private PointF randomPoint(Random random, Dimension dimension) {
		return new PointF(random.nextInt(dimension.getWidth()), random.nextInt(dimension.getHeight()));
	}

	// Randomly walk some distance from current coordinate
	private PointF randomSlop(Random random, PointF p, Dimension dimension) {
		int count = random.nextInt(10);
		int x = Math.round(p.x);
		int y = Math.round(p.y);
		for (int i = 0; i < count; i++) {
			x = (x + (random.nextInt() % 10)) % dimension.getWidth();
			y = (y + (random.nextInt() % 10)) % dimension.getHeight();
		}
		return new PointF(x, y);
	}

	/**
	 * generate a random event based on mFactor
	 */
	private void generateEvents() {
		float cls = mRandom.nextFloat();
		int lastKey = 0;

		if (cls < mFactors[FACTOR_TOUCH]) {
			generatePointerEvent(mRandom, GESTURE_TAP);
			return;
		} else if (cls < mFactors[FACTOR_MOTION]) {
			generatePointerEvent(mRandom, GESTURE_DRAG);
			return;
		} else if (cls < mFactors[FACTOR_PINCHZOOM]) {
			generatePointerEvent(mRandom, GESTURE_PINCH_OR_ZOOM);
			return;
		} 
		// These events are not generated.
		//		else if (cls < mFactors[FACTOR_TRACKBALL]) {
		//			return;
		//		} else if (cls < mFactors[FACTOR_ROTATION]) {
		//			return;
		//		} else if (cls < mFactors[FACTOR_PERMISSION]) {
		//			return;
		//		}

		// The remaining event categories are injected as key events
		// The remaining event categories are injected as key events
		if (cls < mFactors[FACTOR_NAV]) {
			lastKey = NAV_KEYS[mRandom.nextInt(NAV_KEYS.length)];
		} else if (cls < mFactors[FACTOR_MAJORNAV]) {
			lastKey = MAJOR_NAV_KEYS[mRandom.nextInt(MAJOR_NAV_KEYS.length)];
		} else if (cls < mFactors[FACTOR_SYSOPS]) {
			lastKey = SYS_KEYS[mRandom.nextInt(SYS_KEYS.length)];
		} 
		// These events are not generated.
//		else if (cls < mFactors[FACTOR_APPSWITCH]) {
//			MonkeyActivityEvent e = new MonkeyActivityEvent(mMainApps.get(
//					mRandom.nextInt(mMainApps.size())));
//			mQ.addLast(e);
//			return;
//		} 
//		else if (cls < mFactors[FACTOR_FLIP]) {
//			MonkeyFlipEvent e = new MonkeyFlipEvent(mKeyboardOpen);
//			mKeyboardOpen = !mKeyboardOpen;
//			mQ.addLast(e);
//			return;
//		} 
		else {
			// Generate random integer in the range [0, size())
			lastKey = mRandom.nextInt(AndroidKeyCodeWrapper.v().size());
		}
		KeyEvent e = new KeyEvent(AndroidKeyCodeWrapper.v().get(lastKey).getO1());
		mQ.addLast(e);
	}

	public boolean validate() {
		//check factors
		return adjustEventFactors();
	}

	/**
	 * if the queue is empty, we generate events first
	 * @return the first event in the queue
	 */
	public Event getNextEvent() {
		if (mQ.isEmpty()) {
			generateEvents();
		}
		mEventCount++;
		Event e = mQ.getFirst();
		mQ.removeFirst();
		return e;
	}
}
