package testing.ttg;

import java.util.Set;

import org.jgrapht.graph.DirectedPseudograph;

/**
 * Testing trace graph (TTG) records the information of testing.
 * TTGNode represents a program state.
 * TTGEdge represents a state transition via event.
 * 
 * @author yifei
 */
public class TestingTraceGraph {
	private TestingTraceGraph() {}
	
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
	
	public DirectedPseudograph<TTGNode, TTGEdge> getTTG() {
		return ttg;
	}
	
	private DirectedPseudograph<TTGNode, TTGEdge> ttg;
	private static TestingTraceGraph singleton;
}
