package xyz.kpzip.kspice.component.source;

import xyz.kpzip.kspice.circuit.Circuit.ConnectionPoint;
import xyz.kpzip.kspice.component.Abstract2NodeComponent;

/**
 * Represents a voltage source.
 * @author kpzip
 *
 */
public abstract class AbstractSource extends Abstract2NodeComponent {

	/**
	 * Creates a new source
	 * @param first - the first point that this source is connected to
	 * @param second - the second point that this source is connected to
	 */
	public AbstractSource(ConnectionPoint first, ConnectionPoint second) {
		super(first, second);
	}

	@Override
	public final double currentDependence() {
		return 0;
	}

	@Override
	public final double voltageDependence() {
		return 1;
	}

	@Override
	public final double constantDependence() {
		double v = getSourceVoltage();
		return isReversed() ? v : -v;
	}
	
	@Override
	public final void differential(double dt) {
		updateTime(dt);
	}
	
	/**
	 * Function for time varying sources to use to update their stored time value.
	 * @param time - the change in time since the last simulation step.
	 */
	public void updateTime(double time) {}
	
	/**
	 * Gets the voltage of this source.
	 * @return - the voltage of this source in Volts
	 */
	public abstract double getSourceVoltage();

}
