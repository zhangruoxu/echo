package testing.ttg;

import org.jgraph.graph.DefaultEdge;

import testing.event.Event;
import testing.event.inspect.InspectEvent;

/**
 * TTGEdge represents an app state transition achieved by an event.
 * 
 * @author yifei
 */
public class TTGEdge extends DefaultEdge {
	public TTGEdge(TTGNode _source, TTGNode _target, Event _event) {
		source = _source;
		target = _target;
		assert ! (event instanceof InspectEvent);
		event = _event;
	}
	
	public Event getEvent() {
		return event;
	}

	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(! this.getClass().equals(o.getClass()))
			return false;
		TTGEdge edge = (TTGEdge) o;
		if(source.equals(edge.source) && target.equals(edge.target) && event.equals(edge.event))
			return true;
		else return false;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + source.hashCode();
		result = prime * result + target.hashCode();
		result = prime * result + event.hashCode();
		return result;
	}
	
	private Event event;
}
