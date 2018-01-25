package reduction.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.DirectedPseudograph;

import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;
import reduction.ttg.node.NormalState;

/**
 * This class provides utility method being used during TTG reduction.
 * @author yifei
 */
public class TTGReductionHelper {
	// Collect events from TTG.
	public static List<Event> getEvents(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		List<Event> events = new ArrayList<>();
		events.addAll(getEventsFromNode(ttg));
		events.addAll(getEventsFromEdge(ttg));
		// Sort the events according to its ID
		events.sort((e1, e2) -> Integer.compare(e1.getID(), e2.getID()));
		return events;
	}
	
	// Collect events from nodes.
	public static List<Event> getEventsFromNode(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		return ttg.vertexSet().stream().filter(NormalState.class::isInstance).map(NormalState.class::cast)
			.map(NormalState::getEvents).flatMap(Collection::stream).collect(Collectors.toList());
	}

	// Collect events from edges.
	public static List<Event> getEventsFromEdge(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		return ttg.edgeSet().stream().map(TTGEdge::getEvent).collect(Collectors.toList());
	}
}
