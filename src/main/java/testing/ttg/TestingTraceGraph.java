package testing.ttg;

import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

import testing.Layout;
import testing.event.Event;
import testing.ttg.node.NormalState;
import testing.ttg.node.NormalStateFactory;
import testing.ttg.node.TTGNode;

/**
 * Testing trace graph (TTG) records the information of testing.
 * TTGNode represents a program state.
 * TTGEdge represents a state transition via event.
 * 
 * @author yifei
 */
public class TestingTraceGraph {
	public static synchronized TestingTraceGraph v() {
		if(singleton == null)
			singleton = new TestingTraceGraph();
		return singleton;
	}

	public static void reset() {
		singleton = null;
	}

	// Delegate methods of DirectedPseudograph.
	public Set<TTGNode> vertexSet() {
		return ttg.vertexSet();
	}

	public Set<TTGEdge> edgeSet() {
		return ttg.edgeSet();
	}

	public Set<TTGEdge> incomingEdgesOf(TTGNode node) {
		return ttg.incomingEdgesOf(node);
	}

	public Set<TTGEdge> outgoingEdgesOf(TTGNode node) {
		return ttg.outgoingEdgesOf(node);
	}

	// TTG operations
	// Add a new node into TTG
	public void addNewNode(Layout from, boolean isEntry) {
		NormalState fromNode = NormalStateFactory.create(from);
		if(isEntry) fromNode.setAsEntry();
		assert ! ttg.containsVertex(fromNode);
		ttg.addVertex(fromNode);
	}
	
	// Insert an edge into TTG
	public void addEdge(Layout from, Layout to, Event event) {
		NormalState fromNode = NormalStateFactory.getOrCreate(from);
		NormalState toNode = NormalStateFactory.getOrCreate(to);
		TTGEdge edge = new TTGEdge(fromNode, toNode, event);
		if(! ttg.containsEdge(edge)) {
			if(! ttg.containsVertex(fromNode)) ttg.addVertex(fromNode);
			if(! ttg.containsVertex(toNode)) ttg.addVertex(toNode);
			ttg.addEdge(fromNode, toNode, edge);
		}
	}

	// Update the events performed against an existing layout without introducing any layout updates
	public void updateState(Layout layout, Event event) {
		NormalState from = NormalStateFactory.get(layout);
		assert ttg.containsVertex(from);
		from.addEvent(event);
	}

	public DirectedPseudograph<TTGNode, TTGEdge> getTTG() {
		return ttg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(TTGEdge e: ttg.edgeSet()) {
			builder.append(e);
			builder.append("\n");
		}
		return builder.toString();
	}

	private TestingTraceGraph() {
		ttg = new DirectedPseudograph<>(TTGEdge.class);
	}
	
	private DirectedPseudograph<TTGNode, TTGEdge> ttg;
	private static TestingTraceGraph singleton;
}
