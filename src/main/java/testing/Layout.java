package testing;

/**
 * Layout class represents the layout.
 * Currently, it is the content of the XML file that describes a page.
 * 
 * @author yifei
 */
public class Layout {
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
		return layoutContent.equals(o);
	}
	
	public int hashCode() {
		return layoutContent.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Activity: ");
		builder.append(activity);
		builder.append("\n");
		builder.append("Layout:\n");
		builder.append(layoutContent);
		return builder.toString();
	}
	
	private String activity;
	private String layoutContent;
}
