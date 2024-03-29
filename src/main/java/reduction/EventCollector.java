package reduction;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DirectedPseudograph;

import monkey.event.Event;
import reduction.ttg.TTGEdge;
import reduction.ttg.TTGNode;

/**
 * This class collects events on the path generated by PathFinder according to some strategy.
 * 
 * @see reduction.PathFinder
 * @author yifei
 */
public interface EventCollector {
	public abstract List<Event> collectEventsOnPath(DirectedPseudograph<TTGNode, TTGEdge> ttg, GraphPath<TTGNode, TTGEdge> path);
}
