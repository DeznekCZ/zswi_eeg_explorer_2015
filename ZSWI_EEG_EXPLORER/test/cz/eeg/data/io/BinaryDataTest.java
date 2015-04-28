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

	
	


	@Test
	public void testBinaryData() {
		BinaryData.read(new File("input/Masaryko001_20141124.eeg"), 3);
		assertNotNull(BinaryData.getDat());
	}

	@Test
	public void testGetDat() {
		BinaryData.read(new File("input/Masaryko001_20141124.eeg"), 3);
		assertNotNull(BinaryData.getDat());
	}

	@Test
	public void testCtiByte() {
		BinaryData.read(new File("input/Masaryko001_20141124.eeg"), 3);
		assertNotNull(BinaryData.getDat());

	}

}
