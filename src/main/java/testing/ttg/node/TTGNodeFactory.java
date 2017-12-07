package testing.ttg.node;

import java.util.HashMap;
import java.util.Map;

import testing.Layout;

/**
 * This class is a factory for the TTGNode. It manages creation of TTG graph node.
 * It saves a copy of the node which has been created. 
 * The nodes that have been created can be obtained via TTGNodeKey.
 * 
 * @author yifei
 */
public class TTGNodeFactory {
	static {
		nodes = new HashMap<>();
	}
	
	public static boolean contains(Layout layout) {
		return nodes.containsKey(new TTGNodeKey(layout));
	}

	public static TTGNode get(Layout layout) {
		TTGNodeKey key = new TTGNodeKey(layout);
		assert nodes.containsKey(key);
		return nodes.get(key);
	}

	public static TTGNode create(Layout layout) {
		TTGNodeKey key = new TTGNodeKey(layout);
		return create(key);
	}
	
	public static TTGNode getOrCreate(Layout layout) {
		TTGNodeKey key = new TTGNodeKey(layout);
		if(nodes.containsKey(key))
			return nodes.get(key);
		else {
			return create(key);
		}
	}
	
	private static TTGNode create(TTGNodeKey key) {
		assert ! nodes.containsKey(key);
		TTGNode node = new TTGNode(key);
		nodes.put(key, node);
		return node;
	}
	
	// Discard all the nodes.
	public static void reset() {
		nodes.clear();
	}

	private static Map<TTGNodeKey, TTGNode> nodes;
	
	static class TTGNodeKey {
		public TTGNodeKey(Layout _layout) {
			layout = _layout;
		}
		Layout layout;
		
		@Override
		public boolean equals(Object o) {
			if(this == o)
				return true;
			if(o == null)
				return false;
			if(! getClass().equals(o.getClass()))
				return false;
			TTGNodeKey key = (TTGNodeKey) o;
			return layout.equals(key.layout);
		}
		
		@Override
		public int hashCode() {
			return layout.hashCode();
		}
	}
}
