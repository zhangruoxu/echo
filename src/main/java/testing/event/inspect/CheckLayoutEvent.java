package testing.event.inspect;

import org.xmlunit.diff.Diff;

import testing.AppInfoWrapper;
import testing.Env;
import testing.Layout;
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
		Layout lastLayout = env.getLastLayout();
		String lastLayoutContent = null;
		if(lastLayout != null)
			lastLayoutContent = lastLayout.getLayoutContent();
		Diff diff = LayoutComparison.getDiff(curLayoutContent, lastLayoutContent);
		if(diff != null && diff.hasDifferences()) {
			System.out.println("====== Differences with previous page:");
			diff.getDifferences().forEach(Log::println);
			System.out.println("====== End");
		}
		env.appendLayout(new Layout(curLayoutContent));
	}

	@Override
	public String toString() {
		return "[CheckLayoutEvent]";
	}

}
