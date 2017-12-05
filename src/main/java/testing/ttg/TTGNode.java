package testing.ttg;

import java.util.List;

import testing.Layout;
import testing.event.Event;
import testing.event.inspect.InspectEvent;

/**
 * A TTGNode represents a program state during testing. 
 * Program state includes the activity name and the XML layout file content of current page.
 * An event may not trigger state transition. The events that are performed over the same state are aggregated together.
 * 
 * @author yifei
 */
public class TTGNode {
	public TTGNode(String _activityName, Layout _layout) {
		activityName = _activityName;
		layout = _layout;
	}

	public String getActivityName() {
		return activityName;
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
	
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(! this.getClass().equals(o.getClass()))
			return false;
		TTGNode node = (TTGNode) o;
		if(activityName.equals(node.activityName) && layout.equals(node.layout))
			return true;
		else return false;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + activityName.hashCode();
		result = prime * result + layout.hashCode();
		return result;
	}

	private String activityName;
	private Layout layout;
	private List<Event> events;
}
