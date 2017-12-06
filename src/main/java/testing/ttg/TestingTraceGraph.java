package testing.ttg;

import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

import testing.Layout;
import testing.event.Event;

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

	// Add a node into TTG
	public void addNode(Layout from) {
		TTGNode fromNode = new TTGNode(from);
		assert ! ttg.containsVertex(fromNode);
		ttg.addVertex(fromNode);
	}
	
	// TTG operations
	// Insert an edge into TTG
	public void addEdge(Layout from, Layout to, Event event) {
		TTGNode fromNode = new TTGNode(from);
		TTGNode toNode = new TTGNode(to);
		TTGEdge edge = new TTGEdge(fromNode, toNode, event);
		if(! ttg.containsEdge(edge)) {
			// TODO
			// Do we need to check the existence of the fromNode and the toNode in the TTG?
			ttg.addVertex(fromNode);
			ttg.addVertex(toNode);
			ttg.addEdge(fromNode, toNode, edge);
		}
	}

	// Update the events performed against an existing layout without introducing any layout updates
	public void updateState(Layout layout, Event event) {
		TTGNode from = new TTGNode(layout);
		assert ttg.containsVertex(from);
		TTGNode node = getVertex(from);
		node.addEvent(event);
	}

	// Obtain the node that is equal to the given node from TTG
	// TODO
	// More efficient implementation.
	public TTGNode getVertex(TTGNode node) {
		if(ttg.containsVertex(node))
			for(TTGNode n : ttg.vertexSet())
				if(node.equals(n))
					return n;
		return null;
	}

	public DirectedPseudograph<TTGNode, TTGEdge> getTTG() {
		return ttg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		ttg.edgeSet().forEach(builder::append);
		return builder.toString();
	}
	
	private TestingTraceGraph() {
		ttg = new DirectedPseudograph<>(TTGEdge.class);
	}
	
	private DirectedPseudograph<TTGNode, TTGEdge> ttg;
	private static TestingTraceGraph singleton;
}
