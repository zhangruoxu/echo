package testing.event.inspect;

import org.xmlunit.diff.Diff;

import testing.AppInfoWrapper;
import testing.Env;
import testing.Layout;
import testing.ttg.TestingTraceGraph;
import util.LayoutComparison;
import util.Log;

/**
 * This event inspects the layout XML file of current page.
 * Then the layout file is inserted into the layout trace.
 * Current layout compares with the previous one.
 * 
 * @author yifei
 */
public class CheckLayoutEvent extends InspectEvent {
	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curLayoutContent = env.driver().getPageSource();
		Layout curLayout = new Layout(getCurrentActivity(env), curLayoutContent);
		Layout lastLayout = env.getLastLayout();
		String lastLayoutContent = null;
		if(lastLayout == null) {
			// Current layout does not have any predecessor, add the node into TTG
			handleNewState(info, env, curLayout);
		} else {
			// Last layout has been found. Add a new edge into TTG (if layout updates are found) 
			// or update the new event into current TTG node (if layout does not update)
			lastLayoutContent = lastLayout.getLayoutContent();
			Diff diff = LayoutComparison.getDiff(curLayoutContent, lastLayoutContent);
			if(diff != null && diff.hasDifferences()) {
				Log.println("====== Differences with previous page:");
				diff.getDifferences().forEach(Log::println);
				Log.println("====== End");
				handleNewState(info, env, lastLayout, curLayout);
			} else {
				Log.println("Same as the previous layout. ");
				updateExistingState(info, env, lastLayout);
			}
		}
		// Append the layout trace
		env.appendLayout(curLayout);
	}

	@Override
	public String toString() {
		return "[CheckLayoutEvent]";
	}

	// Insert a new node into TTG
	private void handleNewState(AppInfoWrapper info, Env env, Layout layout) {
		TestingTraceGraph.v().addNode(layout);
	}

	// Insert an edge into TTG
	private void handleNewState(AppInfoWrapper appInfo, Env env, Layout from, Layout to) {
		TestingTraceGraph.v().addEdge(from, to, env.getLastEvent());
	}

	// Update an existing node in TTG
	private void updateExistingState(AppInfoWrapper appInfo, Env env, Layout from) {
		TestingTraceGraph.v().updateState(from, env.getLastEvent());
	}
}
