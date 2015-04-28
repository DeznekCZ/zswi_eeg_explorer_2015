package cz.eeg.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.AfterClass;
import org.junit.Test;

import cz.eeg.data.EegFile;
import cz.eeg.data.vhdrmerge.MergeVhdr;

public class FilesIOTest {

	FilesIO fi=new FilesIO();



	@Test
	public void testRead() {
		fail("Not yet implemented");
	}

	@Test
	public void testWrite() throws IOException, FileReadingException {
		assertTrue(	fi.write(FilesIO.read(new File("input/Masarykovo003_20141124.vhdr")),new File("output"), "novy", true));
	}

	@Test
	public void testWrite2() throws IOException, FileReadingException {
		assertTrue(	fi.write(FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr")),new File("output"), "novy-1", true));
	}

	@Test
	public void testBackupDataFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testMergeDataFiles() {
		EegFile i1;
		EegFile i2;
		try {
			i1 = FilesIO.read(new File("input/Masarykovo003_20141124.vhdr"));
			i2 = FilesIO.read(new File("input/Masarykovo001_20141124.vhdr"));
			assertTrue(fi.mergeDataFiles(3, "nove", i1,i2 ));
		} catch (FileReadingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}

	@Test
	public void testMergeDataFiles2() {
		EegFile i1;
		EegFile i2;
		try {
			i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
			i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
			assertTrue(fi.mergeDataFiles(3, "no", i1,i2 ));
		} catch (FileReadingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}

	@Test
	public void testMergeVhdrs() {
		try {
			EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
			EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
			assertTrue(fi.mergeVhdrs(new File("output"),"mergeVHDR", i1,i2));
		} catch (VhdrMergeException | FileReadingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
