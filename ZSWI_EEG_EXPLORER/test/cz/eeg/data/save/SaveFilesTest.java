package cz.eeg.data.save;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;

import cz.eeg.data.vhdrmerge.Vhdr;

public class SaveFilesTest {

	Vhdr vhdrFile;
	
	@Before
	public void setUp() throws Exception {
		vhdrFile = new Vhdr(new File("input/Masaryko002_20141124.vhdr"), true);
	}

	@Test
	public void testReadable() {
		assertTrue(vhdrFile.isReadable());
	}
	
	@Test
	public void testSaveAs(){
		try {
			new SaveFiles("novySoubor", vhdrFile, true);
		} catch (FileNotFoundException e) {
			assertTrue("File is not created.", false);
		} catch (FileAlreadyExistsException e) {
			assertTrue("Overwriting falses.", false);
		}
	}

}
