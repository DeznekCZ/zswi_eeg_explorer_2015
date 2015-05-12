package cz.eeg.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




import org.junit.Test;
import org.omg.CORBA.FREE_MEM;

import cz.eeg.data.EegFile;

public class FilesIOTest {

	FilesIO fi=new FilesIO();



	@Test
	public void isReadable() {
		assertTrue(fi.isReadable(new File("input/Masaryko002_20141124-1.vhdr")));
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
	public void testisMergeable() throws IOException, FileReadingException {
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
		assertTrue(fi.isMergeable(i1, i2,null));
	}
	@Test
	public void testisMergeable2() throws IOException, FileReadingException {
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		assertFalse(fi.isMergeable(i1, i1,null));
	}
	@Test
	public void testisMergeable3() throws IOException, FileReadingException {
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
		i2.setNumberOfChannels(10);
		assertFalse(fi.isMergeable(i1, i2,null));
	}

	@Test
	public void testisMergeable4() throws IOException, FileReadingException {
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
		i2.setSamplingInterval(1);
		assertFalse(fi.isMergeable(i1, i2,null));
	}
	@Test
	public void testisMergeable5() throws IOException, FileReadingException {
		FilesIO f=new FilesIO();
		boolean[] positionTmp={false,false,false,false,false,false,false,false,false,false};
		f.setPositionTmp(positionTmp);
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
		assertFalse(fi.isMergeable(i1, i2,null));
	}

	@Test
	public void testMergeTMP() throws IOException, FileReadingException {
		EegFile i1 = FilesIO.read(new File("input/Masaryko002_20141124-1.vhdr"));
		EegFile i2 = FilesIO.read(new File("input/Masaryko002_20141124-2.vhdr"));
		assertNotNull(fi.mergeTMP(i1,i2));
	}
	@Test
	public void testIsFreespace() throws IOException, FileReadingException {
		assertNotNull(fi.isFreespace());
	}


	@Test
	public void testMergeDataFiles() {
		EegFile i1;
		EegFile i2;
		try {
			i1 = FilesIO.read(new File("input/Masarykovo003_20141124.vhdr"));
			i2 = FilesIO.read(new File("input/Masarykovo001_20141124.vhdr"));
			assertTrue(fi.mergeDataFiles(3,new File("nove.test"), i1,i2 ));
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
			assertTrue(fi.mergeDataFiles(3, new File("no"), i1,i2 ));
		} catch (FileReadingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}

	@Test
	public void testMergeVmrks(){
		EegFile i1;
		EegFile i2;
		try {
			i1 = FilesIO.read(new File("input/Masarykovo003_20141124.vhdr"));
			i2 = FilesIO.read(new File("input/Masarykovo001_20141124.vhdr"));
			assertNotNull(fi.mergeVmrks(i1,i2));

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
			assertNotNull(fi.mergeVhdrs(new File("."),"mergeVHDR", i1,i2));
		} catch (FileReadingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
