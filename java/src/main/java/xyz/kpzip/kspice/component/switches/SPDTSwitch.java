/**
 * 
 */
package xyz.kpzip.kspice.component.switches;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.circuit.Circuit.ConnectionPoint;
import xyz.kpzip.kspice.component.Component;
import xyz.kpzip.kspice.util.CircuitUtil.ConnectionPointPair;

/**
 * 
 * Represents a single-pull double-throw switch with a normally open connector, a normally closed connector and a switching connector.
 * 
 * @author kpzip
 * 
 */
public class SPDTSwitch implements Component, Switch {
	
	private Circuit.ConnectionPoint normallyOpen;
	private Circuit.ConnectionPoint normallyClosed;
	private Circuit.ConnectionPoint switchConnector;
	
	private boolean on;
	
	private double current = 0;

	/**
	 * 
	 */
	public SPDTSwitch(ConnectionPoint normallyOpen, ConnectionPoint normallyClosed, ConnectionPoint switchConnector) {
		this(normallyOpen, normallyClosed, switchConnector, false);
	}

	public SPDTSwitch(ConnectionPoint normallyOpen, ConnectionPoint normallyClosed, ConnectionPoint switchConnector, boolean on) {
		this.normallyOpen = normallyOpen;
		this.normallyClosed = normallyClosed;
		this.switchConnector = switchConnector;
		this.on = on;
	}

	@Override
	public boolean isOn() {
		return on;
	}

	@Override
	public void setOn(boolean on) {
		this.on = on;
	}

	@Override
	public int connectionPointCount() {
		return 3;
	}

	@Override
	public int connectionCount() {
		return 1;
	}

	//TODO make this not have different return values to comply with component specification
	@Override
	public ConnectionPointPair[] connections() {
		return new ConnectionPointPair[] {new ConnectionPointPair(switchConnector, on ? normallyOpen : normallyClosed, this)};
	}

	@Override
	public double[] constraints() {
		return new double[] {0.0d, 1.0d, 0.0d} ;
	}

	@Override
	public void updateCurrent(double[] currents) {
		this.current = currents[0];
	}

	@Override
	public void reset() {
		this.current = 0.0d;
	}
	
	public double getCurrent() {
		return this.current;
	}

}
