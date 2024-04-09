/**
 * 
 */
package xyz.kpzip.kspice.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author kpzip
 * 
 */
public final class ImmutableCollectionWrapper<T> implements Collection<T> {
	
	private final Collection<T> internal;
	
	/**
	 * 
	 */
	private ImmutableCollectionWrapper(Collection<T> c) {
		this.internal = c;
	}
	
	public static final <T> Collection<T> of(Collection<T> c) {
		return new ImmutableCollectionWrapper<T>(c);
	}

	@Override
	public int size() {
		return internal.size();
	}

	@Override
	public boolean isEmpty() {
		return internal.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internal.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return new ImmutableIteratorWrapper<T>(internal.iterator());
	}

	@Override
	public Object[] toArray() {
		return internal.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return internal.toArray(a);
	}

	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internal.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Collection is wrapped as immutable!");
	}
	
	private static class ImmutableIteratorWrapper<T> implements Iterator<T>  {

		private final Iterator<T> internal;
		
		private ImmutableIteratorWrapper(Iterator<T> i) {
			internal = i;
		}
		
		@Override
		public boolean hasNext() {
			return internal.hasNext();
		}

		@Override
		public T next() {
			return internal.next();
		}
		
	}

}
