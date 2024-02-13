package xyz.kpzip.kspice.component;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.circuit.ConnectionPointPair;

public abstract class Abstract2NodeComponent implements Component {

	protected Circuit.ConnectionPoint first;
	protected Circuit.ConnectionPoint second;
	
	protected double current = 0;
	
	public Abstract2NodeComponent(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public final int connectionPointCount() {
		return 2;
	}
	
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
