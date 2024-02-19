/**
 * 
 */
package xyz.kpzip.kspice.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Contains static utilities to help with parsing units.
 * 
 * @author kpzip
 * 
 */
public final class UnitParser {

	private UnitParser() {}
	
	private static final Map<String, Double> MODIFIERS = new HashMap<String, Double>();
	private static final Set<String> UNIT_NAMES = new HashSet<String>();
	
	private static final Pattern UNIT_PATTERN;
	
	static {
		
		//Put all the data for metric prefixes
		MODIFIERS.put("c", 0.01);
		
		//Put all data for SI units
		UNIT_NAMES.add("F");
		UNIT_NAMES.add("f");
		UNIT_NAMES.add("Ohm");
		UNIT_NAMES.add("ohm");
		
		//Make the regex
		StringBuilder pattern = new StringBuilder();
		pattern.append("(");
		for (String str : UNIT_NAMES) {
			//TODO
		}
		UNIT_PATTERN = Pattern.compile(pattern.toString());
	}
	
	public double parseUnit(String input) throws NumberFormatException {
		
		return 0.0;
	}
	
	

}
