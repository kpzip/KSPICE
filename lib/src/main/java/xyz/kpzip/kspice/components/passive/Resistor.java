package xyz.kpzip.kspice.components.passive;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.components.Abstract2NodeComponent;

public class Resistor extends Abstract2NodeComponent {

	private final double resistance;
	
	public Resistor(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second, final double resistance) {
		super(first, second);
		this.resistance = resistance;
	}

	@Override
	public double currentDependence() {
		return resistance;
	}

	@Override
	public double voltageDependence() {
		return -1;
	}
	
	@Override
	public double constantDependence() {
		return 0;
	}
	
	public double getResistance() {
		return resistance;
	}

}
