package reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import heros.solver.Pair;
import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.node.ErrorState;
import reduction.ttg.node.NormalState;
import reduction.ttg.node.TTGNode;

/**
 * This class reduces TTG to replay error.
 * @author yifei
 */
public class TTGReduction {
	// Reduce the testing trace based on TTG.
	public static List<Event> reduce(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		List<Event> events = new ArrayList<>();
		TTGNode from = ttg.vertexSet().stream().filter(TTGNode::isEntry).findFirst().get();
		Optional<TTGNode> opt = ttg.vertexSet().stream().filter(TTGNode::isErrorState).findFirst();
		if(opt.isPresent())
			events = getEventsOnShortestPath(ttg, from, opt.get());
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
