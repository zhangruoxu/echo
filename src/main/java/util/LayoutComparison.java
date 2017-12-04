package util;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonListener;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEngine;

/**
 * This class compares two XML files.
 */
public class LayoutComparison {
	private static DifferenceEngine diffEngine;
	private static ComparisonListener comparisonListener;
	
	static {
		diffEngine = new DOMDifferenceEngine();
		comparisonListener = (comparison, result) -> {
			Log.println("Difference found: " + comparison);
		};
		diffEngine.addDifferenceListener(comparisonListener);
	}
	
	/**
	 * Compare two XML files denoted by a and b.
	 * If one of a and b is null, skip comparison.
	 */
	public static void diff(Object a, Object b) {
		if(a == null || b == null)
			return;
		diffEngine.compare(Input.from(a).build(), Input.from(b).build());
	}
	
	/**
	 * Obtain the differences between two XML files denoted by a and b.
	 */
	public static Diff getDiff(Object a, Object b) {
		if(a == null || b == null)
			return null;
		Diff diff = DiffBuilder.compare(Input.from(a))
				.withTest(Input.from(b))
				.checkForIdentical()
				.ignoreComments()
				.ignoreWhitespace()
				.normalizeWhitespace()
				.build();
		return diff;
	}
	
	/**
	 * Return whether there is difference between two XML files denoted by a and b.
	 */
	public static boolean hasDiff(Object a, Object b) {
		Diff diff = getDiff(a, b);
		if(diff == null)
			return false;
		return getDiff(a, b).hasDifferences();
	}
}
