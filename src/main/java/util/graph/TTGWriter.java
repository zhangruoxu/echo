package util.graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import testing.ttg.TestingTraceGraph;

/**
 * This class dumps TTG to a file.
 * @author yifei
 */
public class TTGWriter {
	// Serialize the TTG.
	public static void serializeTTG(File graphFile) {
		try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(graphFile))) {
			outputStream.writeObject(TestingTraceGraph.v().getTTG());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
