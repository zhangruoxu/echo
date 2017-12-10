package testing.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;
import testing.event.Event;
import testing.ttg.TTGEdge;
import testing.ttg.node.TTGNode;

/**
 * This class reduces TTG to replay error.
 * @author yifei
 */
public class TTGReduction {
	// Find the shortest paths between two nodes in the graph.
	public static <V, E> GraphPath<V, E> shortestPath(DirectedPseudograph<V, E> graph, V from, V to) {
		DijkstraShortestPath<V, E> shortestPath = new DijkstraShortestPath<>(graph);
		return shortestPath.getPath(from, to);
	}

	// Find the reachable events between two nodes in the graph.
	public static List<Event> getEventsOnShortestPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, TTGNode from, TTGNode to) {
		return shortestPath(ttg, from, to).getEdgeList().stream().map(TTGEdge::getEvent).collect(Collectors.toList());
	}

	// Reduce the testing trace based on TTG.
	public static List<Event> reduce(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		List<Event> events = new ArrayList<>();
		TTGNode from = ttg.vertexSet().stream().filter(TTGNode::isEntry).findFirst().get();
		Optional<TTGNode> opt = ttg.vertexSet().stream().filter(TTGNode::isErrorState).findFirst();
		if(opt.isPresent())
			events = getEventsOnShortestPath(ttg, from, opt.get());
		return events;
	}
}
