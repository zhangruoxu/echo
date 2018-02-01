package reduction.event;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidKeyCode;
import monkey.event.KeyEvent;
import monkey.event.Throttle;
import monkey.event.ThrottleEvent;
import monkey.util.AppInfoWrapper;
import monkey.util.Env;
import monkey.util.Logcat;
import reduction.ttg.TestingTraceGraph;
import reduction.ttg.node.NormalState;
import util.Log;

/**
 * This event is used to inspect the current activity
 * 
 * This event can detects whether the testing has been distracted from the app being tested.
 * If that happens, we check whether it is caused by the exceptions and crashes via inspecting logcat entries. 
 * If true, testing terminates. If not, we try to return to the app being tested. 
 * We firstly try to press back button to return. If the app does not return, then the app is restarted. 
 * 
 * The app is restarted via directly launching the first activity (activity traces are kept during testing), 
 * which does not clean the previous data generated during testing. 
 * 
 * @author yifei
 */
public class CheckActivityEvent extends InspectEvent {
	public CheckActivityEvent() {
		super();
	}

	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curAct = getCurrentActivity(env);
		Log.println("# Current activity: " + curAct);
		// If current is in current app, then save it to the current activity trace
		if(info.contains(curAct)) {
			env.appendActivity(env.driver().currentActivity());
			// Event has been successfully injected, obtain log so that it won't appear at next time
			Logcat.getLog();
		} else {
			// Testing has quit from the app being tested
			// Obtain logcat to see whether there are exceptions
			String log = Logcat.getLogAsString();
			if(Logcat.isException(log)) {
				// Error occurs
				// Pint the logcat info then stop testing
				Log.println("App error.");
				Log.println(log);
				env.eventSource().notifyError();
				// Insert an error state into TTG.
				NormalState lastNormalState = TestingTraceGraph.v().getLastNormalState();
				if(lastNormalState == null)
					Log.println("No event trace.");					
				else 
					TestingTraceGraph.v().addErrorState(lastNormalState, env.getLastEvent());
			} else {
				// No error happens. 
				// Try to return to the app being tested
				for(int i = 0; i < 5; i++) {
					if(info.contains(getCurrentActivity(env)))
						break;
					new KeyEvent(AndroidKeyCode.BACK).injectEvent(info, env);
					new ThrottleEvent(Throttle.v().getThrottleDuration() * 2).injectEvent(info, env);
				}
				// If testing does not return to the app, we relaunch the app.
				if(! info.contains(getCurrentActivity(env))) {
					String firstAct = env.getFirstActivity();
					String lastAct = env.getLastActivity();
					if(firstAct != null && firstAct.equals(lastAct)) {
						// Start the first activity during testing.
						// Using the launching app API all the app data are lost.
						// The app is relaunched via starting the first activity in the testing trace.
						Activity mainActivity = new Activity(info.getPkgName(), firstAct);
						env.driver().startActivity(mainActivity);
					}
					else {
						Log.println("# Warning: testing has been distracted from the app " + info.getPkgName());
						System.exit(0);
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "[CheckActivityEvent].";
	}
}
