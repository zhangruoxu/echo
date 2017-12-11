package testing.ttg.node;

import java.util.ArrayList;
import java.util.List;

import testing.Layout;
import testing.event.Event;
import testing.event.inspect.InspectEvent;
import testing.ttg.node.NormalStateFactory.NormalStateKey;

/**
 * A NormalState represents an error-free tesing state during testing. 
 * Program state includes the activity name and the XML layout file content of current page.
 * An event may not trigger state transition. The events that are performed over the same state are aggregated together.
 * 
 * @author yifei
 */
public class NormalState extends TTGNode {
	/**
	 * The constructor can be accessed within the same package.
	 * Only the NormalStateFactory is able to create the NormalState.
	 * Other classes can only create the NormalState via the NormalStateFactory.
	 */
	NormalState(NormalStateKey key) {
		assert key.layout != null;
		entry = false;
		layout = key.layout;
		events = new ArrayList<>();
	}

	public Layout getLayout() {
		return layout;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void addEvent(Event event) {
		assert ! (event instanceof InspectEvent);
		events.add(event);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(! this.getClass().equals(o.getClass()))
			return false;
		NormalState node = (NormalState) o;
		return layout.equals(node.layout);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + layout.hashCode();
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(entry)
			builder.append("#Entry# ");
		builder.append("Activity: ");
		builder.append(layout.getActivity());
		builder.append("Layout: ");
		builder.append(layout.getLayoutContent());
		builder.append(" Event: [");
		for(Event e : events) {
			builder.append(e);
			builder.append(" ");
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	private Layout layout;
	private List<Event> events;
}
