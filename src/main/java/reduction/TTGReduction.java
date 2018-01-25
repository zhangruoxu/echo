package reduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import heros.solver.Pair;
import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;
import reduction.ttg.node.ErrorState;
import reduction.ttg.node.NormalState;

/**
 * This class reduces TTG to replay error.
 * @author yifei
 */
public class TTGReduction {
	// Reduce the testing trace based on TTG.
	public static List<Event> reduce(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		Optional<TTGNode> opt = ttg.vertexSet().stream().filter(TTGNode::isErrorState).findFirst();
		// If error event is not in the TTG, return an empty list
		if(! opt.isPresent())
			return new ArrayList<>();
		// First, find nodes in a path from the entry node to error state node.
		List<TTGNode> nodesOnPath = getPath(ttg);
		// Then, collect events from the path. 
		return getEventsOnPath(ttg, nodesOnPath);
	}
	
	 /**
	  * Return an ordered list which contains all the nodes 
	  * on the path from entry to error state node.
	  */
	private static List<TTGNode> getPath(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		// Find the shortest path from entry event to error event
		GraphPath<TTGNode, TTGEdge> path = shortestPath(ttg);
		return path.getVertexList();
	}
	
	/**
	 * Collect events on the paths given by an ordered node list.
	 * 
	 * Analysis starts here --> ...
	 *                           ^
	 *                           | <-- curNodeOutgoingEdge
	 *                           A <-- curNode
	 *                           ^
	 *                           | <-- curNodeIncomingEdge
	 *                          ...
	 */ 
	private static List<Event> getEventsOnPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, List<TTGNode> nodesOnPath) {
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
	
	// Find the shortest paths between the entry node to the error state node on the TTG.
	public static GraphPath<TTGNode, TTGEdge> shortestPath(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		Pair<NormalState, ErrorState> pair = getNodes(ttg);
		return shortestPath(ttg, pair.getO1(), pair.getO2());
	}

	// Find the reachable events between two nodes on the graph.
	public static List<Event> getEventsOnShortestPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, TTGNode from, TTGNode to) {
		return shortestPath(ttg, from, to).getEdgeList().stream().map(TTGEdge::getEvent).collect(Collectors.toList());
	}

	// Find the reachable events from the entry node to the error state on the TTG.
	public static List<Event> getEventsOnShortestPath(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		Pair<NormalState, ErrorState> pair = getNodes(ttg);
		return getEventsOnShortestPath(ttg, pair.getO1(), pair.getO2());
	}

	// Find the shortest paths between two nodes on the graph.
	// Use Dijkstra algorithm to find the shortest path.
	private static <V, E> GraphPath<V, E> shortestPath(DirectedPseudograph<V, E> graph, V from, V to) {
		DijkstraShortestPath<V, E> shortestPath = new DijkstraShortestPath<>(graph);
		return shortestPath.getPath(from, to);
	}

	// Return the entry node and the error state node on the TTG.
	private static Pair<NormalState, ErrorState> getNodes(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		Optional<TTGNode> fromOpt = ttg.vertexSet().stream().filter(TTGNode::isEntry).findFirst();
		Optional<TTGNode> toOpt = ttg.vertexSet().stream().filter(TTGNode::isErrorState).findFirst();
		if(! fromOpt.isPresent())
			throw new RuntimeException("Entry state node is not found. ");
		if(! toOpt.isPresent())
			throw new RuntimeException("Error state node is not found. ");
		return new Pair<NormalState, ErrorState>((NormalState) fromOpt.get(), (ErrorState) toOpt.get());
	}
}
