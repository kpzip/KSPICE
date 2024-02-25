/**
 * 
 */
package xyz.kpzip.kspice.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import xyz.kpzip.kspice.util.UnitParser;

/**
 * 
 * @author kpzip
 * 
 */
final class UtilTests {

	private UtilTests() {}
	
	private static final double EPSILON = 0.0000001;
	
	@Test
	void unitParsingTest() {
		double val1 = UnitParser.parseUnit("1.0cm");
		assertEquals(val1, 0.01, EPSILON);
		
		double val2 = UnitParser.parseUnit("100.0 µF");
		assertEquals(val2, 0.0001, EPSILON);
		
		double val3 = UnitParser.parseUnit("5 Megs");
		assertEquals(val3, 5000000, EPSILON);
		
		double val4 = UnitParser.parseUnit("40u");
		assertEquals(val4, 0.00004, EPSILON);
	}

}
