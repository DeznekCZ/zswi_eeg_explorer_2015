package cz.eeg.data.io;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import cz.eeg.io.BinaryData;
import cz.eeg.io.FilesIO;

public class BinaryDataTest {

	BinaryData dataFile=new BinaryData(new File("input/Masaryko001_20141124.eeg"), 3);
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}



	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBinaryData() {
		assertNotNull(dataFile);
	}

	@Test
	public void testGetDat() {
		//dataFile.getDat();
		assertArrayEquals(dataFile.getDat(), new BinaryData(new File("input/Masaryko001_20141124.eeg"), 3).getDat());
	}

	@Test
	public void testCtiByte() {
		assertNull(dataFile.getDat()[0]);

	}

}
