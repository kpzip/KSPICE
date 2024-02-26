package xyz.kpzip.kspice.component.switches;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.Abstract2NodeComponent;

/**
 * a single-pull single-throw switch.
 * 
 * @author kpzip
 *
 */
public class SPSTSwitch extends Abstract2NodeComponent implements Switch {
	
	private boolean on;

	public SPSTSwitch(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		this(first, second, false);
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
	
	@Override
	public boolean isOn() {
		return on;
	}
	
	@Override
	public void setOn(boolean on) {
		this.on = on;
	}

}
