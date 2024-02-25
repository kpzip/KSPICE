package xyz.kpzip.kspice.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.Component;

/**
 * Contains static methods and static utility classes for circuit simulation
 * 
 * @author kpzip
 * 
 */
public final class CircuitUtil {

	private CircuitUtil() {}
	
	
	/**
	 * 
	 * @author kpzip
	 * 
	 */
	public static class SimulationFrame implements Comparable<SimulationFrame> {
		
		public Map<Circuit.ConnectionPoint, Double> voltages;
		public Map<Component, double[]> currents;

		/**
		 * 
		 */
		public SimulationFrame() {
			voltages = new HashMap<Circuit.ConnectionPoint, Double>();
			currents = new HashMap<Component, double[]>();
		}
		
		/**
		 * 
		 * @param prev
		 * @return
		 */
		public double maxVariance(SimulationFrame prev) {
			double max = 0;
			for (Entry<Circuit.ConnectionPoint, Double> e : voltages.entrySet()) {
				
				//Assumes that no new connection points have been created
				double first = prev.voltages.get(e.getKey());
				double second = e.getValue();
				double difference = Math.abs(first - second);
				if (difference > max) max = difference;
			}
			
			for (Entry<Component, double[]> e : currents.entrySet()) {
				
				//TODO account for connection changes
				double[] first = prev.currents.get(e.getKey());
				double[] second = e.getValue();
				for (int i = 0; i < first.length; i++) {
					double difference = Math.abs(first[i] - second[i]);
					if (difference > max) max = difference;
				}
			}
			
			return max;
			
		}

		@Override
		public int compareTo(SimulationFrame o) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		

	}
	
	
	
	/**
	 * Wrapper class
	 * 
	 * @author kpzip
	 * 
	 */
	public static class ConnectionPointPair {

		public final Circuit.ConnectionPoint first;
		public final Circuit.ConnectionPoint second;
		public final Component host;
		
		public ConnectionPointPair(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second, Component host) {
			if (first.compareTo(second) > 0) {
				this.first = first;
				this.second = second;
			}
			else {
				this.first = second;
				this.second = first;
			}
			this.host = host;
		}

		@Override
		public int hashCode() {
			return Objects.hash(first, second, host);
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
			return first == other.first && second == other.second && host == other.host;
		}
		
		@Override
		public String toString() {
			return first.toString() + " " + second.toString();
		}

	}

}
