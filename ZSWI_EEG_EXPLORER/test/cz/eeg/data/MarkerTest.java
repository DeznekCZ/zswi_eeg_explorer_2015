package cz.eeg.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MarkerTest  {

	Marker mk=new Marker("Mk6=Stimulus,S  6,19469,0,0");
	
	@Test
	public void testGetMarkerNumber() {
		assertEquals(6,mk.getMarkerNumber());
	}

	@Test
	public void testGetType() {
		assertEquals("Stimulus",mk.getType());
	}

	@Test
	public void testGetDescription() {
		assertEquals("S  6",mk.getDescription());
	}

	@Test
	public void testGetPositionInDataPoints() {
		assertEquals("19469",mk.getPositionInDataPoints());
	}


	@Test
	public void testGetSizeInDataPoints() {
		assertEquals("0",mk.getSizeInDataPoints());
	}



	@Test
	public void testGetChannelNumber() {
		assertEquals("0",mk.getChannelNumber());
	}




}
