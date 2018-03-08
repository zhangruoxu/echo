package reduction.ttg;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import io.appium.java_client.android.AndroidKeyCode;
import monkey.Main;
import monkey.TestUtil;
import monkey.event.Event;
import monkey.event.KeyEvent;
import monkey.event.Throttle;
import monkey.event.ThrottleEvent;
import monkey.exception.TestFailureException;
import monkey.util.AppInfoWrapper;
import reduction.DijkstraShortestPathFinder;
import reduction.EventCollector;
import reduction.PathEventCollector;
import reduction.PathFinder;
import reduction.TTGReduction;
import reduction.WeightedGraphShorthestPathFinder;
import reduction.checker.ResultChecker;
import reduction.ttg.TTGEdge;
import reduction.util.TTGReductionHelper;
import soot.toolkits.scalar.Pair;
import util.Config;
import util.graph.TTGReader;

/**
 * Test the TTG and testing trace reduction.
 * @author yifei
 */
public class TestTTG {

	private void printTTGInfo(DirectedPseudograph<TTGNode, TTGEdge> ttg) {
		GraphPath<TTGNode, TTGEdge> shortestPath = DijkstraShortestPathFinder.shortestPath(ttg);
		System.out.println("# Nodes: " + ttg.vertexSet().size());
		System.out.println("# Edges: " + ttg.edgeSet().size());
		System.out.println("# Shortest path length: " + shortestPath.getLength());
		System.out.println(TestingTraceGraph.toString(ttg));
	}

	// Testing the app 1.
	@Test
	public void test1() {
		String[] args = new String[] {"-app", "1", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		monkey.Main.main(args);
	}

	// Replay the app 1.
	@Test
	public void replay1() {
		testingTTG(1);
		AppInfoWrapper appInfo = new AppInfoWrapper(1);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		Main.replay(appInfo, ttg);
	}

	// Testing the app 4.
	@Test
	public void test4() {
		String[] args = new String[] {"-app", "4", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		monkey.Main.main(args);
	}

	// Replay the app 4.
	@Test
	public void replay4() {
		testingTTG(4);
		AppInfoWrapper appInfo = new AppInfoWrapper(4);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		GraphPath<TTGNode, TTGEdge> path = TTGReduction.shortestPath(ttg);
		//		System.out.println("# Nodes: " + ttg.vertexSet().size());
		//		System.out.println("# Edges: " + ttg.edgeSet().size());
		//		System.out.println("# Path length: " + path.getLength());
		//		System.out.println("# Node on the path: ");
		//		path.getVertexList().forEach(System.out::println);
		//		System.out.println("# Events: ");
		//		TTGReductionHelper.getEvents(ttg).forEach(System.out::println);
		//		System.out.println(TestingTraceGraph.toString(ttg));
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	// Testing the app 6.
	@Test
	public void test6() {
		String[] args = new String[] {"-app", "6", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		monkey.Main.main(args);
	}

	// Replay the app 6.
	@Test
	public void replay6() {
		testingTTG(6);
		AppInfoWrapper appInfo = new AppInfoWrapper(6);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Main.replay(appInfo, ttg);
	}

	// Testing the app 7.
	// No bug found up to 5000 events.
	@Test
	public void test7() {
		String[] args = new String[] {"-app", "7", "-event",  "10000", "-throttle", "200", "-seed", "0"};
		monkey.Main.main(args);
	}

	// Replay app 7.
	@Test
	public void replay7() {
		testingTTG(7);
		AppInfoWrapper appInfo = new AppInfoWrapper(7);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	// Replay app 10.
	@Test
	public void replay10() {
		testingTTG(10);
		AppInfoWrapper appInfo = new AppInfoWrapper(10);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Class<? extends PathFinder> pathFinder = WeightedGraphShorthestPathFinder.class;
		Class<? extends EventCollector> eventCollector = PathEventCollector.class;
		ResultChecker.check(ttg, pathFinder, eventCollector);
		// Main.replay(appInfo, ttg, pathFinder, eventCollector);
	}

	@Test
	public void replay12() {
		testingTTG(12);
		AppInfoWrapper appInfo = new AppInfoWrapper(12);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay16() {
		testingTTG(16);
		AppInfoWrapper appInfo = new AppInfoWrapper(16);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
		ResultChecker.check(ttg, DijkstraShortestPathFinder.class, PathEventCollector.class);
	}

	@Test
	public void replay31() {
		testingTTG(31);
		AppInfoWrapper appInfo = new AppInfoWrapper(31);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay32() {
		testingTTG(32);
		AppInfoWrapper appInfo = new AppInfoWrapper(32);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay33() {
		testingTTG(33);
		AppInfoWrapper appInfo = new AppInfoWrapper(33);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay34() {
		testingTTG(34);
		AppInfoWrapper appInfo = new AppInfoWrapper(34);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay35() {
		testingTTG(35);
		AppInfoWrapper appInfo = new AppInfoWrapper(35);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay36() {
		testingTTG(36);
		AppInfoWrapper appInfo = new AppInfoWrapper(36);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay39() {
		testingTTG(39);
		AppInfoWrapper appInfo = new AppInfoWrapper(39);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay45() {
		testingTTG(45);
		AppInfoWrapper appInfo = new AppInfoWrapper(45);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		printTTGInfo(ttg);
		// Initialize throttle time
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay48() {
		testingTTG(48);
		AppInfoWrapper appInfo = new AppInfoWrapper(48);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		// printTTGInfo(ttg);
		// Initialize throttle time
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay53() {
		testingTTG(53);
		AppInfoWrapper appInfo = new AppInfoWrapper(53);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		// printTTGInfo(ttg);
		// Initialize throttle time
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay54() {
		testingTTG(54);
		AppInfoWrapper appInfo = new AppInfoWrapper(54);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		// printTTGInfo(ttg);
		// Initialize throttle time
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	@Test
	public void replay68() {
		testingTTG(68);
		AppInfoWrapper appInfo = new AppInfoWrapper(68);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		//		printTTGInfo(ttg);
		// Initialize throttle time
		Throttle.v().init(500);
		Main.replay(appInfo, ttg);
	}

	// Load and inspect TTG.
	// TTG node number
	// TTG edge number
	// Event on node
	// Event on edge
	@Test
	public void loadTTG() {
		Config.init(null);
		for(File file : new File(Config.v().get(Config.OUTPUT)).listFiles()) {
			if(! file.isDirectory())
				continue;
			if(! new File(String.join(File.separator, file.getAbsolutePath(), "graph")).exists())
				continue;
			Integer appId = Integer.valueOf(file.getName().substring(0, file.getName().indexOf("_")));
			AppInfoWrapper appInfoWrapper = new AppInfoWrapper(appId);
			DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfoWrapper);
			List<Number> ttgStat = new ArrayList<>();
			ttgStat.add(ttg.vertexSet().size());
			ttgStat.add(ttg.edgeSet().size());
			ttgStat.add(TTGReductionHelper.getEventsFromNode(ttg).size());
			ttgStat.add(TTGReductionHelper.getEventsFromEdge(ttg).size());
			System.out.println(file.getName() + "\t" + String.join("\t", ttgStat.stream().map(Number::toString).collect(Collectors.toList())));
		}
	}

	public DirectedPseudograph<TTGNode, TTGEdge> getTTG(AppInfoWrapper appInfo) {
		String path = String.join(File.separator, Config.v().get(Config.OUTPUT), appInfo.getAppName());
		File graphFile = new File(path, "graph");
		return TTGReader.deserializeTTG(graphFile);
	}

	private void testingTTG(int id) {
		Config.init(null);
		AppInfoWrapper appInfo = new AppInfoWrapper(id);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		System.out.println("#Node: " + ttg.vertexSet().size());
		System.out.println("#Edge: " + ttg.edgeSet().size());
		System.out.println("#Events on node: " + TTGReductionHelper.getEventsFromNode(ttg).size());
		System.out.println("#Events on edge: " + TTGReductionHelper.getEventsFromEdge(ttg).size());
		//		List<Event> reducedEvents = TTGReduction.reduce(ttg, DijkstraShortestPathFinder.class, SimpleEventCollector.class);
		//		System.out.println("#Events after reduction: " + reducedEvents.size());
		//		System.out.println("Events:");
		//		reducedEvents.forEach(System.out::println);		
	}

	/**
	 * The minimal events that can trigger the bug.
	 */
	@Test
	public void testReplay4() {
		List<Event> events = new ArrayList<>();
		ThrottleEvent throttleEvent = new ThrottleEvent(500);
		events.add(new KeyEvent(AndroidKeyCode.KEYCODE_MENU));
		events.add(new KeyEvent(AndroidKeyCode.KEYCODE_DPAD_CENTER));
		events.add(new KeyEvent(AndroidKeyCode.KEYCODE_DPAD_CENTER));
		events.add(new KeyEvent(AndroidKeyCode.KEYCODE_BUTTON_8));
		TestUtil.initTesting("4", (info, env) -> {
			try {
				for(Event event : events) {
					event.injectEvent(info, env);
					throttleEvent.injectEvent(info, env);
				}
			}catch (TestFailureException e) {
				// TODO: handle exception
			}
		} );
	}

	// ==============================================================================
	// Find reduction bug in app 4
	@Test
	public void findBugs() {
		Config.init(null);
		DirectedPseudograph<TTGNode, TTGEdge> success = 
				TTGReader.deserializeTTG(new File(String.join(File.separator, Config.v().get(Config.OUTPUT), "4_Addi-debug-success", "graph")));
		DirectedPseudograph<TTGNode, TTGEdge> fail = 
				TTGReader.deserializeTTG(new File(String.join(File.separator, Config.v().get(Config.OUTPUT), "4_Addi-debug-fail", "graph")));

		System.out.println(DijkstraShortestPathFinder.getEventsOnShortestPath(success));
	}
}
