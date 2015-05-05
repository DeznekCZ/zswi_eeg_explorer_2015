package cz.deznekcz.reflect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OutTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testInteger() {
		int i = 8;
		Out<Integer> out = new Out<Integer>(i);
		
		assertEquals("Value not equals:", i, out.value().intValue());
	}

	@Test
	public void testToString() {
		String value = "make a text";
		Out<String> out = new Out<String>(value);
		
		assertEquals("Value not equals:", "Reference: <"+value+">", out.toString());
	}

	@Test
	public void testNullToString() {
		Out<String> out = new Out<String>();
		
		assertEquals("Value not equals:", "Reference: <null>", out.toString());
	}

	@Test
	public void testNull() {
		Out<String> out = new Out<String>();
		
		assertEquals("Value not equals:", "Reference: <null>", out.toString());
	}

	@Test
	public void testEquals() {
		String testString = "test";
		Out<String> outConst = new Out<String>(testString);
		
		Out<String> outLock = new Out<String>();
		outLock.lock(testString);
		
		assertTrue("Value not equals:", outConst.equals(outLock));
	}

	@Test
	public void testEqualsToNull() {
		String testString = "test";
		Out<String> outConst = new Out<String>(testString);
		
		assertFalse("Value not equals:", outConst.equals(null));
	}

	@Test
	public void testEqualsToNullValue() {
		Out<String> out1 = new Out<String>("string1");
		Out<String> out2 = new Out<String>();
		
		assertFalse("Value not equals:", out1.equals(out2));
	}

	@Test
	public void testEqualsNullValueTo() {
		Out<String> out1 = new Out<String>(null);
		Out<String> out2 = new Out<String>("string2");
		
		assertFalse("Value not equals:", out1.equals(out2));
	}

	@Test
	public void testEqualsAnotherValues() {
		Out<String> out1 = new Out<String>("string1");
		Out<String> out2 = new Out<String>("string2");
		
		assertFalse("Value not equals:", out1.equals(out2));
	}

	@Test
	public void testEqualsAnotherObject() {
		Out<String> outConst = new Out<String>();
		
		Integer anotherObject = 5;
		
		assertFalse("Value not equals:", outConst.equals(anotherObject));
	}
}
