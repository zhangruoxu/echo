package reduction;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DirectedPseudograph;

import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;

public class ShortestPathEventCollector implements EventCollector {
	@Override
	// TODO
	public List<Event> collectEventsOnPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, GraphPath<TTGNode, TTGEdge> path) {
		List<TTGNode> nodesOnPath = path.getVertexList();
		List<TTGEdge> edgesOnPath = path.getEdgeList();
		assert nodesOnPath.size() == edgesOnPath.size() - 1;
		List<Event> events = new ArrayList<>();
		return events;
	}
}
