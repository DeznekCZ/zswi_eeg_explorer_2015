package cz.eeg.data;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import cz.eeg.io.FilesIO;

public class EegFileTest extends EegFile {

	EegFile vhdrFile;
	
	

	@Before
	public void setUp() throws Exception {
		vhdrFile = FilesIO.read(new File("input/Masarykovo001_20141124.vhdr"));
	}



	@Test
	public void testEegFile() {
		assertNotNull(vhdrFile);
	}

	@Test
	public void testGetVhdrData() {
		assertNotNull(vhdrFile.getVhdrData());
	}

	@Test
	public void testIsReadable() {
		vhdrFile.setReadable(true);
		assertTrue(vhdrFile.isReadable());
	}

	@Test
	public void testGetDataFormat() {
		assertNotNull(vhdrFile.getDataFormat());
	}

	@Test
	public void testGetDataOrient() {
		assertNotNull(vhdrFile.getDataOrient());
	}

	@Test
	public void testGetNumberOfChannels() {
		vhdrFile.setNumberOfChannels(2);
		assertEquals(2,vhdrFile.getNumberOfChannels());
	}

	@Test
	public void testGetSamplingInterval() {
		assertNotNull(vhdrFile.getSamplingInterval());
	}

	@Test
	public void testGetBinaryFormat() {
		assertNotNull(vhdrFile.getBinaryFormat());
	}

	@Test
	public void testGetDataFileName() {
		assertNotNull(vhdrFile.getDataFileName());
	}

	@Test
	public void testGetMarkerFileName() {
		assertNotNull(vhdrFile.getMarkerFileName());
	}

	@Test
	public void testGetCodePage() {
		assertNotNull(vhdrFile.getCodePage());
	}

	@Test
	public void testGetChannel() {
		assertNotNull(vhdrFile.getChannel());
	}

	@Test
	public void testIsEditable() {
		assertTrue(vhdrFile.isEditable());
	}

	@Test
	public void testIsSaveable() {
		assertTrue(vhdrFile.isSaveable());
	}

	@Test
	public void testIsCloseable() {
		assertTrue(vhdrFile.isCloseable());
	}

	@Test
	public void testGetVmrkData() {
		assertNotNull(vhdrFile.getVmrkData());
	}

	@Test
	public void testGetDataRead() {
		assertNotNull(vhdrFile.getDataRead());
	}

	@Test
	public void testGetName() {
		vhdrFile.setHeaderFile(new File("path/Jmeno.vhdr"));;
		assertEquals("Jmeno.vhdr", vhdrFile.getName());
	}

	@Test
	public void testGetDataFile() {
		assertNotNull(vhdrFile.getDataFile());
	}

	@Test
	public void testGetMarkerFile() {
		assertNotNull(vhdrFile.getMarkerFile());
	}


}
