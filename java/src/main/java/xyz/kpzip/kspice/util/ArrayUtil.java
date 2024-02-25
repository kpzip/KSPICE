/**
 * 
 */
package xyz.kpzip.kspice.util;

/**
 * 
 * Static utilities for working with arrays not provided in the Java Arrays class
 * 
 * @author kpzip
 * 
 * @see java.util.Arrays
 * 
 */
public class ArrayUtil {

	private ArrayUtil() {}
	
	/**
	 * Linear search an array, using .equals() method for comparison.
	 * 
	 * @param <T> - the type of elements in the array
	 * @param element - the element to be searched for
	 * @param array - the array to be searched
	 * @return - the index if the element is found, or -1 if it is not found.
	 */
	public static <T> int linearSearchEq(T element, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(element)) return i;
		}
		return -1;
	}
	
	/**
	 * Linear search an array, comparing object references
	 * 
	 * @param <T> - the type of elements in the array
	 * @param element - the element to be searched for
	 * @param array - the array to be searched
	 * @return - the index if the element is found, or -1 if it is not found.
	 */
	public static <T> int linearSearchRef(T element, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element) return i;
		}
		return -1;
	}

}
