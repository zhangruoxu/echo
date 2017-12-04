package testing.event.inspect;

import testing.AppInfoWrapper;
import testing.Env;
import util.LayoutComparison;

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
		LayoutComparison.diff(curLayout, lastLayout);
		env.appendLayout(curLayout);
	}

	@Override
	public String toString() {
		return "[CheckLayoutEvent]";
	}

}
