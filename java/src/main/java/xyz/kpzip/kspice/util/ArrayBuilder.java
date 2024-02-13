package xyz.kpzip.kspice.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArrayBuilder<T> {

	private List<T[]> arrays;
	
	//seriously, this makes me want to quit java for good
	private Class<T> tclass;
	
	public ArrayBuilder(Class<T> tclass) {
		arrays = new LinkedList<T[]>();
		this.tclass = tclass;
	}
	
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
