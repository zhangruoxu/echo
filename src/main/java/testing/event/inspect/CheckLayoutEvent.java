package testing.event.inspect;

import org.xmlunit.diff.Diff;

import testing.AppInfoWrapper;
import testing.Env;
import util.LayoutComparison;
import util.Log;

/**
 * This event inspects the layout XML file of current page.
 * Then the layout file is inserted into the layout trace.
 * Current layout compares with the previous one.
 */
public class CheckLayoutEvent extends InspectEvent {
	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		String curLayout = env.driver().getPageSource();
		String lastLayout = env.getLastLayout();
		Diff diff = LayoutComparison.getDiff(curLayout, lastLayout);
		if(diff != null && diff.hasDifferences()) {
			System.out.println("====== Differences with previous page:");
			diff.getDifferences().forEach(Log::println);
			System.out.println("====== End");
		}
		env.appendLayout(curLayout);
	}

	@Override
	public String toString() {
		return "[CheckLayoutEvent]";
	}

}
