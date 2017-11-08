package testing.event;

import java.util.LinkedList;

import testing.event.inspect.CheckActivityEvent;

/**
 * Queue of testing events.
 * 
 * This class is adapted from the class MonkeyEventQueue of the Android Open Source Project.
 * 
 * @author yifei
 */

@SuppressWarnings("serial")
public class EventQueue extends LinkedList<Event> {
	public EventQueue() {
		super();
	}

	@Override
	public void addLast(Event e) {
		super.add(e);
		// Insert a fix time delay after some events to let the GUI respond to the event
		if (e.isThrottlable()) {
			super.add(new ThrottleEvent());
		}
		/**
		 * Check current activity during testing after injecting every event
		 * TODO Better way to insert inspecting events
		 * @author yifei
		 */
		super.add(new CheckActivityEvent());
	}
}
