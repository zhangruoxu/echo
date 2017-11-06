package testing.event;

import java.util.ArrayList;
import java.util.List;

import util.PointF;

/**
 * MotionEvent
 * Touch, long press, zoom, pinch, swipe.
 * 
 * This class is adapted from the class MonkeyMotionEvent of the Android Open Source Project.
 * 
 * @author yifei
 */
/**
 * monkey motion event
 */
public abstract class MotionEvent extends Event {
	private long mDownTime = -1;
	private long mEventTime = -1;    
	private int mAction = -1;
	//    private float mX = -1;
	//    private float mY = -1;
	private float mPressure = -1;
	private float mSize = -1;
	private int mMetaState = -1;
	private float mXPrecision = -1;
	private float mYPrecision = -1;
	private int mDeviceId = -1;
	private int mEdgeFlags = -1;

	/**
	 * fromPointers is a list of coordinates that represent the starting points of a motion event.
	 * For a single touch event, there is only one coordinate.
	 * For a multitouch event, there are several coordinates.
	 */
	protected List<PointF> mFromPointers;
	/**
	 * mMoves represents moving from the coordinates in mPointers.
	 * For a simple touch event, like tap and long pressing, this list is empty.
	 * For a moving touch event, like zooming, pinching and swiping, it represents a moving vector. 
	 */
	protected List<PointF> mMoves;

	//If true, this is an intermediate step (more verbose logging, only)
	private boolean mIntermediateNote;  

	public MotionEvent(int type, long downAt, int metaState) {
		super(type);
		mDownTime = downAt;
		mMetaState = metaState;
		mFromPointers = new ArrayList<>();
		mMoves = new ArrayList<>();
	}

	// Add starting pointer coordinate of a motion event
	public MotionEvent addPointer(int id, PointF p) {
		mFromPointers.add(p);
		return this;
	}

	// Add the ending pointer coordinate of a motion event
	private MotionEvent addMove(int id, PointF p) {
		mMoves.add(p);
		return this;
	}

	// Add a moving touch event
	public MotionEvent addFromTo(int id, PointF p, PointF move) {
		addPointer(id, p);
		addMove(id, move);
		return this;
	}

	public void setIntermediateNote(boolean b) {
		mIntermediateNote = b;
	}

	public boolean getIntermediateNote() {
		return mIntermediateNote;
	}

//	public int getAction() {
//		return mAction;
//	}
//
//	public long getDownTime() {
//		return mDownTime;
//	}
//
//	public long getEventTime() {
//		return mEventTime;
//	}
//
//	public void setDownTime(long downTime) {
//		mDownTime = downTime;
//	}
//
//	public void setEventTime(long eventTime) {
//		mEventTime = eventTime;
//	}

	@Override
	public boolean isThrottlable() {
		return false;
	}
}