package reduction;

import java.util.List;
import java.util.Optional;

import org.jgrapht.graph.DirectedPseudograph;

import heros.solver.Pair;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;
import reduction.ttg.node.ErrorState;
import reduction.ttg.node.NormalState;

/**
 * This class generates a path from the entry to error state 
 * in the TTG according to some strategy.
 * 
 * @author yifei
 */
public interface PathFinder {
	public abstract List<TTGNode> findPath(DirectedPseudograph<TTGNode, TTGEdge> ttg);
	
	// Return the entry node and the error state node on the TTG.
	public static Pair<NormalState, ErrorState> getNodes(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		Optional<TTGNode> fromOpt = ttg.vertexSet().stream().filter(TTGNode::isEntry).findFirst();
		Optional<TTGNode> toOpt = ttg.vertexSet().stream().filter(TTGNode::isErrorState).findFirst();
		if(! fromOpt.isPresent())
			throw new RuntimeException("Entry state node is not found. ");
		if(! toOpt.isPresent())
			throw new RuntimeException("Error state node is not found. ");
		return new Pair<NormalState, ErrorState>((NormalState) fromOpt.get(), (ErrorState) toOpt.get());
	}
}
