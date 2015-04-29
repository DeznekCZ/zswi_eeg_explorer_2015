package cz.deznekcz.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

/**
 * {@link EqualArrayList} test case for {@link JUnit4}
 * @author Zdeněk Novotný (DeznekCZ)
 *
 */
public class EAL_Test {
	
	private EqualArrayList<EAL_Integer> integerList;

	@Before
	public void setUp() throws Exception {
		integerList = new EqualArrayList<EAL_Integer>();
		integerList.add(new EAL_Integer());
		integerList.add(new EAL_Integer(1));
		integerList.add(new EAL_Integer("8"));
		integerList.add(new EAL_Integer(3));
	}

	@Test
	public void testInteger() {
		assertTrue(integerList.contains(8));
	}

	@Test
	public void testSameClass() {
		assertTrue(integerList.contains(new EAL_Integer(0)));
	}

	@Test
	public void testString() {
		assertTrue(integerList.contains("1"));
	}

}
