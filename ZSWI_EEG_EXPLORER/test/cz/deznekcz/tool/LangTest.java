package cz.deznekcz.tool;

import static cz.deznekcz.tool.Lang.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <b>cz.deznekcz.tool.Lang</b>
 * @author Zdeněk Novotný (DeznekCZ)
 * @version 2.4
 */
public class LangTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		LANGgererate(); //USE TO CLEAR
		
		LANGload("spanish");
	}

	@Test
	public void testAdding() throws FileNotFoundException {
		assertEquals(loadFile("lang/spanish.lng"), LANGlist());
		
		LANG("data_1");
		LANG("data_2");
		LANG("data_3");
		
		LANGgererate();
		assertEquals(loadFile("lang/spanish.lng"), LANGlist());
		
		assertEquals("<data_1>", LANG("data_1"));
	}

	@Test
	public void testGenerate() {
		assertEquals(true, LANGgererate());
	}
	
	@Test
	public void testSetting() {
		final String data1 = "datas without params";
		LANGset("data_1", data1);
		assertEquals(data1, LANG("data_1"));

		final int data2int = 10;
		final double data2double = 0.74651;
		final String data2 = "datas with integer %3d and float %.2f";
		LANGset("data_2", data2);
		System.out.println("printed data_2: " + LANG("data_2", data2int, data2double));
		assertEquals(
				String.format(data2, data2int, data2double), 
				LANG("data_2", data2int, data2double));
	}
	
	@Test
	public void testBetterSetting() {
		LANG("data_4", new Time(0));
		assertEquals("<data_4,java.sql.Time>", LANG("data_4"));
		LANG("data_5", 5.0f, 6.0f);
		assertEquals("<data_5,java.lang.Float,java.lang.Float>", LANG("data_5"));
		LANG("data_6", 5 + 7);
		assertEquals("<data_6,java.lang.Integer>", LANG("data_6"));
	}
	
	@Test
	public void testMultilined() {
		String value = "Line1/n/Line2/n/Line3";
		
		LANGset("data_7", value);
		
		assertEquals("Line1\nLine2\nLine3", LANGlined("data_7"));
		
		System.out.println("unlined = " + LANG("data_7"));
		System.out.println("lined = " + LANGlined("data_7"));
	}

	private String loadFile(String path) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(path));
		String lines = "";
		
		while(scanner.hasNextLine()) {
			lines += scanner.nextLine() + "\n";
		}
		
		scanner.close();
		return lines;
	}
	
	@After
	public void tearDown() {
		//System.out.print(LANGlist());
	}
}
