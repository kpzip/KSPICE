package xyz.kpzip.kspice.components.switches;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.components.Abstract2NodeComponent;

public class SPSTSwitch extends Abstract2NodeComponent {
	
	private boolean on;

	public SPSTSwitch(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		super(first, second);
		on = false;
	}
	
	public SPSTSwitch(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second, boolean on) {
		super(first, second);
		this.on = on;
	}

	@Override
	public double currentDependence() {
		return on ? 0.0 : 1.0;
	}

	@Override
	public double voltageDependence() {
		return on ? 1.0 : 0.0;
	}

	@Override
	public double constantDependence() {
		return 0;
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void toggle() {
		on ^= on;
	}
	
	public void setOn(boolean on) {
		this.on = on;
	}
	
	public void setOn() {
		this.on = true;
	}

}
