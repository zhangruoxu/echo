package testing.ttg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DirectedPseudograph;
import org.junit.Test;

import io.appium.java_client.android.AndroidKeyCode;
import testing.AppInfoWrapper;
import testing.Main;
import testing.TestUtil;
import testing.event.Event;
import testing.event.KeyEvent;
import testing.event.ThrottleEvent;
import testing.reduction.TestingTraceReduction;
import testing.ttg.node.TTGNode;
import util.Config;
import util.graph.TTGReader;

public class TestTTG {
	@Test
	public void test1() {
		Config.init(null);
		AppInfoWrapper appInfo = new AppInfoWrapper(4);
		DirectedPseudograph<TTGNode, TTGEdge> graph = getTTG(appInfo);
		Main.replay(appInfo, graph);
	}
	
	@Test
	public void test2() {
		Config.init(null);
		AppInfoWrapper appInfo = new AppInfoWrapper(4);
		DirectedPseudograph<TTGNode, TTGEdge> graph = getTTG(appInfo);
		TestingTraceReduction.reduce(graph).forEach(System.out::println);
	}
	
	public DirectedPseudograph<TTGNode, TTGEdge> getTTG(AppInfoWrapper appInfo) {
		String path = String.join(File.separator, Config.v().get(Config.OUTPUT), appInfo.getAppName());
		File graphFile = new File(path, "graph");
		return TTGReader.deserializeTTG(graphFile);
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
}
