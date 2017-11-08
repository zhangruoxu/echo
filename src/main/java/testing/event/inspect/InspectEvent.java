package testing.event.inspect;

import testing.event.Event;

/**
 * This class is the base class of the events that inspecting testing status.
 * 
 * @author yifei
 */
public abstract class InspectEvent extends Event {
	public InspectEvent() {
		super(Event.EVENT_INSPECT);
	}
};
