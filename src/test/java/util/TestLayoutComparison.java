package util;

import java.io.File;

import org.junit.Test;
import org.xmlunit.diff.Diff;

/**
 * Test the class LayOutComparison
 */
public class TestLayoutComparison {
	@Test
	public void test1() {
		LayoutComparison.diff(new File("1.xml"), new File("2.xml"));
	}
	
	@Test
	public void test2() {
		LayoutComparison.diff(new File("1.xml"), new File("1.xml"));
	}
	
	@Test
	public void test3() {
		System.out.println(LayoutComparison.hasDiff(new File("1.xml"), new File("2.xml")));
	}
	
	@Test
	public void test4() {
		Diff diff = LayoutComparison.getDiff(new File("1.xml"), new File("2.xml"));
		diff.getDifferences().forEach(System.out::println);
	}
}
