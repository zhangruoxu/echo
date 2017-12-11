package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonListener;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.DifferenceEngine;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.util.Predicate;

import testing.Layout;

/**
 * This class compares two XML files.
 * @author yifei
 */
public class LayoutComparison {
	private static DifferenceEngine diffEngine;
	// A lambda expression, which is a listener once difference is found.
	private static ComparisonListener comparisonListener;
	// A lambda expression, which defines the comparison. 
	// It is used to ignore some attributes in the XML file during comparison.
	private static DifferenceEvaluator diffEvaluator;
	// A lambda Expression, which is used to filter out the attributes that are not considered during comparison.
	private static Predicate<Attr> attrFilter;
	// A set that contains the name of the attributes which are ignorable during comparison.
	private final static Set<String> ignoreAttributes;

	static {
		ignoreAttributes = new HashSet<>(Arrays.asList(
					"focused"));
		
		diffEngine = new DOMDifferenceEngine();
		
		comparisonListener = (comparison, result) -> {
			Log.println("Difference found: " + comparison);
		};

		diffEngine.addDifferenceListener(comparisonListener);
		
		diffEvaluator = (comparison, outcome) -> {
			if(outcome == ComparisonResult.EQUAL)
				return ComparisonResult.EQUAL;
			final Node controlNode = comparison.getControlDetails().getTarget();
			if(controlNode instanceof Attr) {
				Attr attr = (Attr) controlNode;
				// apply ignorable XML attributes
				if(ignoreAttributes.contains(attr.getName()))
					return ComparisonResult.EQUAL;
			}
			return outcome;
		};

		attrFilter = (attr) -> {
			if(ignoreAttributes.contains(attr.getName()))
					return false;
			else return true;
		};
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
	 * This method defines whether two XML files are the same. 
	 * Some labels or attributes in the XML files can be ignored during comparison, 
	 * like the color of text view.
	 * 
	 * TODO
	 * Ignore some labels or attributes during comparison.
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
				// apply the difference evaluator
//				.withDifferenceEvaluator(diffEvaluator)
				.withAttributeFilter(attrFilter)
				.build();
		return diff;
	}

	/**
	 * Return whether there is difference between two XML files denoted by a and b.
	 */
	public static boolean hasDiff(Object a, Object b) {
		Diff diff = getDiff(a, b);
		if(diff == null)
			return true;
		return getDiff(a, b).hasDifferences();
	}
	
	/**
	 * Because the XML comparison applies special difference evaluator. 
	 * The hashCode() method also needs to apply the same difference evaluator 
	 * so that the hash code of two equally XML strings are the same. 
	 * 
	 * FIXME
	 */
	public static int hashCode(Layout layout) {
		final int prime = 31;
		int result = 1;
		result = prime * result + layout.getActivity().hashCode();
		result = prime * result + layout.getLayoutContent().hashCode();
		return result;
	}
}
