package xyz.kpzip.kspice.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an array builder. Similar to a {@link java.lang.StringBuilder StringBuilder} 
 * since it allows arrays to be added and them converted to a larger array containing all sub arrays in sequence.
 * Useful if you want to join together a bunch of small arrays in a somewhat efficient manner.
 * 
 * @author kpzip
 * 
 * @param <T> The type of elements stored in the arrays to be joined. If you wanted to join a bunch of Integer arrays, T would be Integer
 */
public class ArrayBuilder<T> {

	private List<T[]> arrays;
	
	//seriously, this makes me want to quit java for good
	private Class<T> tclass;
	
	/**
	 * creates a new, empty array builder with the default initial capacity.
	 * @param tclass - the class of the type parameter T. This is required to make a new array of type T. If you wanted to join String arrays, 
	 * this would be equal to String.getClass()
	 */
	public ArrayBuilder(Class<T> tclass) {
		arrays = new ArrayList<T[]>();
		this.tclass = tclass;
	}
	
	/**
	 * creates a new, empty array builder with the specified initial capacity. Use if you know how many arrays you plan to join.
	 * @param tclass - the class of the type parameter T. This is required to make a new array of type T. If you wanted to join String arrays, 
	 * this would be equal to String.getClass()
	 * @param capacity - the initial capacity of this ArrayBuilder.
	 */
	public ArrayBuilder(Class<T> tclass, int capacity) {
		arrays = new ArrayList<T[]>(capacity);
		this.tclass = tclass;
	}
	
	public void pushArray(T[] arr) {
		arrays.add(arr);
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		int length = arrays.stream().mapToInt((arr) -> arr.length).sum();
		T[] arr = (T[]) Array.newInstance(tclass, length);
		int ptr = 0;
		for (T[] t : arrays) {
			for (int i = 0; i < t.length; i++) {
				arr[ptr + i] = t[i];
			}
			ptr += t.length;
		}
		return arr;
	}
	
	public void reverse() {
		for (T[] arr : arrays) {
			for(int i = 0; i < arr.length / 2; i++) {
			    T temp = arr[i];
			    arr[i] = arr[arr.length - i - 1];
			    arr[arr.length - i - 1] = temp;
			}
		}
		Collections.reverse(arrays);
	}

}
