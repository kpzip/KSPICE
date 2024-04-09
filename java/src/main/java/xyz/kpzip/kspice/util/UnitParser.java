/**
 * 
 */
package xyz.kpzip.kspice.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains static utilities to help with parsing units.
 * 
 * @author kpzip
 * 
 */
public final class UnitParser {

	private UnitParser() {}
	
	private static final Map<String, Double> MODIFIERS = new HashMap<String, Double>();
	
	static {
		
		//Store data for all the metric prefixes
		//The really big and really small ones are pretty useless for this
		//however they are here for completeness sake
		MODIFIERS.put("Q", Math.pow(10, 30));
		MODIFIERS.put("R", Math.pow(10, 27));
		MODIFIERS.put("Y", Math.pow(10, 24));
		MODIFIERS.put("Z", Math.pow(10, 21));
		MODIFIERS.put("E", Math.pow(10, 18));
		MODIFIERS.put("P", Math.pow(10, 15));
		MODIFIERS.put("T", Math.pow(10, 12));
		MODIFIERS.put("G", Math.pow(10, 9));
		MODIFIERS.put("M", Math.pow(10, 6));
		MODIFIERS.put("k", Math.pow(10, 3));
		MODIFIERS.put("h", Math.pow(10, 2));
		MODIFIERS.put("da", Math.pow(10, 1));
		MODIFIERS.put("d", Math.pow(10, -1));
		MODIFIERS.put("c", Math.pow(10, -2));
		MODIFIERS.put("m", Math.pow(10, -3));
		MODIFIERS.put("µ", Math.pow(10, -6));
		MODIFIERS.put("n", Math.pow(10, -9));
		MODIFIERS.put("p", Math.pow(10, -12));
		MODIFIERS.put("f", Math.pow(10, -15));
		MODIFIERS.put("a", Math.pow(10, -18));
		MODIFIERS.put("z", Math.pow(10, -21));
		MODIFIERS.put("y", Math.pow(10, -24));
		MODIFIERS.put("r", Math.pow(10, -27));
		MODIFIERS.put("q", Math.pow(10, -30));
		
		//Aliases
		MODIFIERS.put("u", Math.pow(10, -6));
		MODIFIERS.put("Meg", Math.pow(10, 6));
		MODIFIERS.put("K", Math.pow(10, 3));
		

	}
	
	/**
	 * Allows for parsing of simple unit expressions.<br>
	 * This is designed to allow for users to input strings that look like "2 Meg" or "10k"
	 * and get back numbers in SI base units. For example an input of "1.1u" will return a 
	 * value of 1.1 * 10^-6, corresponding with the "µ" prefix. This method also supports common 
	 * aliases like "Meg" for 10^6 and "u" for 10^-6 (so that people don't have to copy the character µ)
	 * 
	 * @implNote for units like grams where the SI unit is not the same as the base unit (Kg vs g), the units of this
	 * method's return value will be the base unit (e.g. grams instead of Kilograms)
	 * 
	 * @param input - the string containing a number and potentially a unit symbol
	 * @return - the value entered in the string, scaled by its metric prefix multiplier
	 * @throws NumberFormatException if the input is egregiously malformatted to the point where no useful information can be extracted.
	 * this is done to attempt to stop bad data from being parsed and interpreted randomly.
	 */
	public static double parseUnit(String input) throws NumberFormatException {
		
		boolean decimal = false;
		int ptr;
		
		//find the number portion of the string, stopping when a non number character is reached 
		//or when a second decimal point is found
		for (ptr = 0; ptr < input.length(); ptr++) {
			char c = input.charAt(ptr);
			if (Character.isDigit(c)) {
				continue;
			}
			if (c == '.' && !decimal) {
				decimal = true;
				continue;
			}
			break;
		}
		double number = Double.parseDouble(input.substring(0, ptr));

		//Parse metric prefixes
outter:	for (; ptr < input.length(); ptr++) {
			for (String prefix : MODIFIERS.keySet()) {
				if (input.substring(ptr).startsWith(prefix)) {
					number *= MODIFIERS.get(prefix);
					break outter;
				}
			}
			
			//If the first letter based characters are garbage, break to avoid parsing bad data
			if (Character.isAlphabetic(input.charAt(ptr))) {
				break outter;
			}
		}
		
		return number;
	}

}
