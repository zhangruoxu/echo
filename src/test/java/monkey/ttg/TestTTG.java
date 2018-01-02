package monkey.ttg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import io.appium.java_client.android.AndroidKeyCode;
import monkey.Main;
import monkey.TestUtil;
import monkey.event.Event;
import monkey.event.KeyEvent;
import monkey.event.ThrottleEvent;
import monkey.util.AppInfoWrapper;
import reduction.TTGReduction;
import reduction.ttg.TTGEdge;
import reduction.ttg.node.TTGNode;
import reduction.util.TTGReductionHelper;
import util.Config;
import util.graph.TTGReader;

/**
 * Test the TTG and testing trace reduction.
 * @author yifei
 */
public class TestTTG {
	
	// Testing the app 4.
	@Test
	public void test4() {
		testingTTG(4);
		AppInfoWrapper appInfo = new AppInfoWrapper(4);
		DirectedPseudograph<TTGNode, TTGEdge> ttg = getTTG(appInfo);
		Main.replay(appInfo, ttg);
	}
	
	// Testing app 7.
	@Test
	public void test7() {
		testingTTG(7);
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
		System.out.println("#Events before reduction: " + TTGReductionHelper.getEvents(ttg).size());
		System.out.println("#Events on node: " + TTGReductionHelper.getEventsFromNode(ttg).size());
		System.out.println("#Events on edge: " + TTGReductionHelper.getEventsFromEdge(ttg).size());
		List<Event> reducedEvents = TTGReduction.reduce(ttg);
		System.out.println("#Events after reduction: " + reducedEvents.size());
		System.out.println("Events:");
		reducedEvents.forEach(System.out::println);		
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
			for(Event event : events) {
				event.injectEvent(info, env);
				throttleEvent.injectEvent(info, env);
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
		
		System.out.println(TTGReduction.getEventsOnShortestPath(success));
	}
}
