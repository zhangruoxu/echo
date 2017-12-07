package testing.ttg.node;

import java.util.HashMap;
import java.util.Map;

import testing.Layout;

/**
 * This class is a factory for the NormalState. It manages creation of TTG graph node.
 * It saves a copy of the node which has been created. 
 * The nodes that have been created can be obtained via NormalStateKey.
 * 
 * @author yifei
 */
public class NormalStateFactory {
	static {
		nodes = new HashMap<>();
	}
	
	public static boolean contains(Layout layout) {
		return nodes.containsKey(new NormalStateKey(layout));
	}

	public static NormalState get(Layout layout) {
		NormalStateKey key = new NormalStateKey(layout);
		assert nodes.containsKey(key);
		return nodes.get(key);
	}

	public static NormalState create(Layout layout) {
		NormalStateKey key = new NormalStateKey(layout);
		return create(key);
	}
	
	public static NormalState getOrCreate(Layout layout) {
		NormalStateKey key = new NormalStateKey(layout);
		if(nodes.containsKey(key))
			return nodes.get(key);
		else {
			return create(key);
		}
	}
	
	private static NormalState create(NormalStateKey key) {
		assert ! nodes.containsKey(key);
		NormalState node = new NormalState(key);
		nodes.put(key, node);
		return node;
	}
	
	// Discard all the nodes.
	public static void reset() {
		nodes.clear();
	}

	private static Map<NormalStateKey, NormalState> nodes;
	
	static class NormalStateKey {
		public NormalStateKey(Layout _layout) {
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
			NormalStateKey key = (NormalStateKey) o;
			return layout.equals(key.layout);
		}
		
		@Override
		public int hashCode() {
			return layout.hashCode();
		}
	}
}
