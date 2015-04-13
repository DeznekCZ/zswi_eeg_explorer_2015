package cz.eeg.data.save;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;

import cz.eeg.data.Vhdr;
import cz.eeg.io.FilesIO;

public class SaveFilesTest {

	Vhdr vhdrFile;
	
	@Before
	public void setUp() throws Exception {
		vhdrFile = FilesIO.read(new File("input/Masaryko002_20141124.vhdr"));
	}

	@Test
	public void testReadable() {
		assertTrue(vhdrFile.isReadable());
	}
	
	@Test
	public void testSaveAs(){
		try {
			new RenameAndSave(new File("."),"novySoubor", vhdrFile, true);
		} catch (FileNotFoundException e) {
			assertTrue("File is not created.", false);
		} catch (FileAlreadyExistsException e) {
			assertTrue("Overwriting falses.", false);
		}
	}

}
