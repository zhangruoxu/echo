package util.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.jgrapht.graph.DirectedPseudograph;

import testing.ttg.TTGEdge;
import testing.ttg.node.TTGNode;

/**
 * This class constructs the TTG via a file.
 * @author yifei
 */
public class TTGReader {
	// Deserialize the TTG.
	@SuppressWarnings("unchecked")
	public static DirectedPseudograph<TTGNode, TTGEdge> deserializeTTG(File f) {
		DirectedPseudograph<TTGNode, TTGEdge> graph = null;
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(f))) {
			graph = (DirectedPseudograph<TTGNode, TTGEdge>) input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}
}
