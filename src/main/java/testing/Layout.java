package testing;

import java.io.Serializable;

import util.LayoutComparison;

/**
 * Layout class represents the layout.
 * Currently, it is the content of the XML file that describes a page.
 * 
 * @author yifei
 */
public class Layout implements Serializable {
	public Layout(String _activity, String _layoutContent) {
		assert _activity != null;
		assert _layoutContent != null;
		activity = _activity;
		layoutContent = _layoutContent;
	}
	
	public String getLayoutContent() {
		return layoutContent;
	}
	
	public String getActivity() {
		return activity;
	}

	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(! this.getClass().equals(o.getClass()))
			return false;
		Layout layout = (Layout) o;
		return activity.equals(layout.activity) && 
				! LayoutComparison.hasDiff(layoutContent, layout.layoutContent);
	}
	
	public int hashCode() {
		return LayoutComparison.hashCode(this);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Activity: ");
		builder.append(activity);
		builder.append(" Layout: ");
		builder.append(layoutContent);
		return builder.toString();
	}
	
	private String activity;
	private String layoutContent;
}
