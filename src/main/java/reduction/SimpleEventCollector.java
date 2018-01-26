package reduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.DirectedPseudograph;

import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;
import reduction.ttg.node.NormalState;

/**
 * This class collects events on the paths given by an ordered node list.
 * 
 * Analysis starts here --> ErrorState
 *                             ...
 *                              ^
 *                              | <-- curNodeOutgoingEdge
 *                              A <-- curNode
 *                              ^
 *                              | <-- curNodeIncomingEdge
 *                             ...
 *                            Entry
 * 
 * 1. Collect the event on the curNodeOutgoingEdge. 
 * 2. Find the incoming edge of curNode with the largest event ID.
 * 3. Find the events on the curNode which happen between the curNodeIncomingEdge and curNodeOutgoingEdge.
 * 4. Find the precursor node and go back to step 1.
 * 
 * @author yifei
 */ 
public class SimpleEventCollector implements EventCollector {

	@Override
	public List<Event> collectEventsOnPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, List<TTGNode> nodesOnPath) {
		List<Event> events = new ArrayList<>();
		// Iterate the node list in reverse. 
		// To begin with, the error state does not have outgoing edges.
		TTGEdge curNodeOutgoingEdge = null;
		for(int i = nodesOnPath.size() - 1; i > 0; i--) {
			TTGNode curNode = nodesOnPath.get(i);
			TTGNode precursorNode = nodesOnPath.get(i - 1);
			/**
			 * In the incoming edges of current node, find the edge with largest event ID. 
			 * This strategy finds the edges which is more close to the error than other edges.
			 */ 
			List<TTGEdge> curNodeIncomingEdges = ttg.getAllEdges(precursorNode, curNode)
					.stream()
					.sorted((e1, e2) -> Integer.compare(e2.getEvent().getID(), e1.getEvent().getID()))
					.collect(Collectors.toList());
			TTGEdge curNodeIncomingEdge = curNodeIncomingEdges.get(0);
			/**
			 * Collect all the identity events occurs between 
			 * the incoming and outgoing edge of current node.
			 * 
			 * If current node has outgoing edge (not the error state), 
			 * add the event stored in the current node whose ID 
			 * is greater than the ID of the event on the incoming edge 
			 * and less than the ID of the event on the outgoing edge. 
			 * If current node does not have outgoing edge (error state), 
			 * find its precursor and continue.
			 */ 
			if(curNodeOutgoingEdge != null) {
				int curNodeIncomingEdgeEventID = curNodeIncomingEdge.getEvent().getID();
				int curNodeOutgoingEdgeEventID = curNodeOutgoingEdge.getEvent().getID();
				assert curNode instanceof NormalState;
				List<Event> curNodeEvents = ((NormalState) curNode).getEvents();
				curNodeEvents.sort((e1, e2) -> Integer.compare(e1.getID(), e2.getID()));
				for(int j = curNodeEvents.size() - 1; j >= 0; j--) {
					Event e = curNodeEvents.get(j);
					if(e.getID() > curNodeIncomingEdgeEventID && e.getID() < curNodeOutgoingEdgeEventID)
						events.add(e);
				}
			}
			// Add the event on the incoming edge.
			events.add(curNodeIncomingEdge.getEvent());
			/**
			 * The outgoing edge of node visited next time 
			 * is the incoming edge of current node. 
			 */
			curNodeOutgoingEdge = curNodeIncomingEdge;
		}
		/**
		 * Because the nodes are visited and the events are collected in reverse order, 
		 * the event list 
		 */
		Collections.reverse(events);
		return events;
	}
}
