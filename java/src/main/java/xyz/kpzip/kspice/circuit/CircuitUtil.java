package xyz.kpzip.kspice.circuit;

import java.util.Objects;

/**
 * Contains static methods and static utility classes for circuit simulation
 * 
 * @Author kpzip
 * 
 */
public final class CircuitUtil {

	private CircuitUtil() {}
	
	
	
	
	
	
	/**
	 * Wrapper class
	 */
	public static class ConnectionPointPair {

		public final Circuit.ConnectionPoint first;
		public final Circuit.ConnectionPoint second;
		
		public ConnectionPointPair(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
			if (first.compareTo(second) > 0) {
				this.first = first;
				this.second = second;
			}
			else {
				this.first = second;
				this.second = first;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(first, second);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConnectionPointPair other = (ConnectionPointPair) obj;
			return Objects.equals(first, other.first) && Objects.equals(second, other.second);
		}
		
		@Override
		public String toString() {
			return first.toString() + " " + second.toString();
		}

	}

}
