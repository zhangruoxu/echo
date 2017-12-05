package testing;

/**
 * Layout class represents the layout.
 * Currently, it is the content of the XML file that describes a page.
 * 
 * @author yifei
 */
public class Layout {
	public Layout(String layout) {
		layoutContent = layout;
	}
	
	public String getLayoutContent() {
		return layoutContent;
	}
	
	public boolean equals(Object o) {
		return layoutContent.equals(o);
	}
	
	public int hashCode() {
		return layoutContent.hashCode();
	}
	
	public String toString() {
		return layoutContent;
	}
	
	private String layoutContent;
}
