package tests;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import utilities.Utilities;
import data.Reading;

public class UtilitiesTest {

	@Test
	public void testConvertRSSIDecToDbm() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateReadingAverage() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateReading() {
		
		String singleReading = "REP 170 0E FF 33 22 11 00 33 22 11 00 11 52 52 52 52 18 B1";
		double expectedAverageRssiDbM = -36.0;
		Reading reading = Utilities.createReading(singleReading);
		double result = reading.getRssiDbm();
		
		assertEquals(expectedAverageRssiDbM, result, 0.5);
	}

}
