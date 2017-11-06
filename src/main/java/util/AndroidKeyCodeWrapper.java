package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.toolkits.scalar.Pair;

/**
 * This class converts key code to corresponding key name.
 * This class stores the pairs of key code and its name.
 * 
 * @author yifei
 */
public class AndroidKeyCodeWrapper {
	public static AndroidKeyCodeWrapper v() {
		return SingletonHolder.singleton;
	}

	public int size() {
		return list.size();
	}

	public List<Pair<Integer, String>> getList() {
		return list;
	}

	public String getKeyCodeName(int i) {
		return keyCode2KeyName.get(i);
	}
	
	public Pair<Integer, String> get(int i) {
		assert i >= 0 && i < list.size();
		return list.get(i);
	}

	private AndroidKeyCodeWrapper() {
		list = new ArrayList<>();
		keyCode2KeyName = new HashMap<>();
		// Obtain all the key codes via reflection
		try {
			for(Field f : Class.forName("io.appium.java_client.android.AndroidKeyCode").getFields()) {
				String name = f.getName();
				if(name.startsWith("KEYCODE")) {
					int keyCode = f.getInt(null);
					list.add(new Pair<>(keyCode, name));
					keyCode2KeyName.put(keyCode, name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// Sort the key code and name pair according to the key code
		list.sort(new Comparator<Pair<Integer, String>>() {
			@Override
			public int compare(Pair<Integer, String> x, Pair<Integer, String> y) {
				return x.getO1().compareTo(y.getO1());
			}
		});
	}

	private List<Pair<Integer, String>> list;
	private Map<Integer, String> keyCode2KeyName;

	/**
	 * Multithread-safe singleton holder.
	 */
	private static class SingletonHolder {
		private static AndroidKeyCodeWrapper singleton = new AndroidKeyCodeWrapper();
	}
}
