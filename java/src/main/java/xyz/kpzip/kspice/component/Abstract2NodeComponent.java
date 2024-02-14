package xyz.kpzip.kspice.component;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.circuit.ConnectionPointPair;

/**
 * Represents a component with 2 connections.
 * handles current flow, and connection point references.
 * @see xyz.kpzip.kspice.component.passive.Resistor
 * @see xyz.kpzip.kspice.component.passive.Capacitor
 * @see xyz.kpzip.kspice.component.passive.Inductor
 * @see xyz.kpzip.kspice.component.source.AbstractSource
 * @see xyz.kpzip.kspice.component.source.Battery
 * @author kpzip
 *
 */
public abstract class Abstract2NodeComponent implements Component {

	protected Circuit.ConnectionPoint first;
	protected Circuit.ConnectionPoint second;
	
	protected double current = 0;
	
	/**
	 * Must be called by subclasses in order to initialize connection point references
	 * @param first - the first connection point to connect to
	 * @param second - the second connection point to connect to
	 */
	public Abstract2NodeComponent(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @implNote any 2 'Node' component only connects 2 connection points, by definition
	 */
	@Override
	public final int connectionPointCount() {
		return 2;
	}
	
	/**
	 * @implNote 2 'Node' components can only have 1 path for current to flow through them.
	 */
	@Override
	public final int connectionCount() {
		return 1;
	}
	
	public abstract double currentDependence();
	
	public abstract double voltageDependence();
	
	public abstract double constantDependence();
	
	@Override
	public double[] constraints() {
		return new double[] {currentDependence(), voltageDependence(), constantDependence()};
	}
	
	@Override
	public final ConnectionPointPair[] connections() {
		return new ConnectionPointPair[] {new ConnectionPointPair(first, second)};
	}
	
	@Override
	public void reset() {
		current = 0;
	}
	
	@Override
	public void updateCurrent(double[] currents) {
		current = currents[0];
	}
	
	public double getCurrent() {
		return current;
	}
	
	public boolean isReversed() {
		return first.compareTo(second) < 0;
	}

}
