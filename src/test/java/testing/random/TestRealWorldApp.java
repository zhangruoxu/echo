package testing.random;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import testing.Logcat;
import testing.TestUtil;
import testing.TestingOptions;
import testing.event.Throttle;
import testing.ttg.TestingTraceGraph;
import testing.ttg.node.TTGNode;
import util.Config;
import util.Log;
import util.Timer;

public class TestRealWorldApp {
	// Test app
	@Test
	public void testApp0() {
		testApp(0,  10000, 0);
	}
	
	// Bug found
	@Test
	public void testApp1() {
		String[] args = new String[] {"-app", "1", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		testing.Main.main(args);
	}
	
	// Appium crashes
	@Test
	public void testApp2() {
		testApp(2,  10000, 0);
	}
	
	// Bug found
	@Test
	public void testApp3() {
		String[] args = new String[] {"-app", "3", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		testing.Main.main(args);
	}
	
	// Bug found
	@Test
	public void testApp4() {
		String[] args = new String[] {"-app", "4", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		testing.Main.main(args);
		Set<TTGNode> nodes = TestingTraceGraph.v().vertexSet();
		System.out.println("TTG");
		System.out.println(TestingTraceGraph.v());
		for(TTGNode n : nodes) {
			System.out.println("# Node: " + n);
			System.out.println("# Outging edges: ");
			TestingTraceGraph.v().outgoingEdgesOf(n).forEach(System.out::println);
		} 
	}
	
	// No bugs found
	@Test
	public void testApp5() {
		testApp(5,  10000, 0);
	}

	// This is a game
	@Test
	public void testApp6() {
		testApp(6,  10000, 0);
	}

	// NullPointerException
	@Test
	public void testApp7() {
		String[] args = new String[] {"-app", "7", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		testing.Main.main(args);
	}

	// No bugs found up to 1177 events
	// This application is simple (only one activity)
	@Test
	public void testApp8() {
		testApp(8,  10000, 0);
	}
	
	// No bugs found up to 1064 events
	@Test
	public void testApp9() {
		testApp(9,  10000, 0);
	}

	// Bug found
	@Test
	public void testApp10() {
		String[] args = new String[] {"-app", "10", "-event",  "5000", "-throttle", "500", "-seed", "8888"};
		testing.Main.main(args);
	}
	
	// No bug found up to 1215 events
	@Test
	public void testApp11() {
		String[] args = new String[] {"-app", "11", "-event",  "5000", "-throttle", "500", "-seed", "0"};
		testing.Main.main(args);
	}
	
	// App 12 A game. NullPointerException
	// App 13 ClassCastException
	// App 14 No exception found up to 1650 events
	// App 15 This app is simple. Show info. 
	// App 16 Make phone call. Simple app. 
	// App 17 A Game. No exception found up to 1310 events
	// App 18 A File explorer. App is simple.
	// App 19 
	@Test
	public void testApp12To22() {
		for(int i = 19; i <= 22; i++) {
			String[] args = new String[] {"-app", Integer.toString(i), "-event",  "5000", "-throttle", "500", "-seed", "0"};
			testing.Main.main(args);
		}
	}
	
	/**
	 * Test Buggy apps
	 */
	@Test
	public void testBuggyApps() {
		List<String> buggyAppIDs = Arrays.asList(
				"10", "11", "12", "13", "14", "15", "16", 
				"1", "25", "29", "30", "31", "32", "33", "34", 
				"35", "36", "39", "3", "44", "45", "48", "4", 
				"51", "53", "54", "5", "61", "68", "6", "7", "9");
		for(String id : buggyAppIDs) {
			String[] args = new String[] {"-app", id, "-event",  "5000", "-throttle", "500", "-seed", "0"};
			testing.Main.main(args);
		}
	}
	
	/**
	 * Test the real-world app
	 */
	private void testApp(int i, int events, int seed) {
		Timer timer = new Timer();
		timer.start();
		TestUtil.initTesting(Integer.toString(i), (info, env) -> {
			System.out.println(info.getPkgName());
			Logcat.clean();
			Throttle.v().init(500);
			TestingOptions.v().setNumberOfEvents(events);
			String output = Config.v().get(Config.OUTPUT);
			File outputDir = new File(output);
			if(! outputDir.exists())
				outputDir.mkdir();
			String fileName = String.join(File.separator, output, info.getAppFileName() + ".txt");
			try(PrintStream printStream = new PrintStream(fileName)) {
				Log.init(printStream);
				RandomEventSource eventSource = new RandomEventSource(info, env, seed);
				eventSource.runTestingCycles();
				timer.stop();
				Log.println("# Time: " + timer.getDurationInSecond() + " s.");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		});
	}
}
