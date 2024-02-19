/**
 * 
 */
package xyz.kpzip.kspice.component.source.voltage;

import xyz.kpzip.kspice.circuit.Circuit.ConnectionPoint;
import xyz.kpzip.kspice.component.source.AbstractSource;

/**
 * 
 * @author kpzip
 * 
 */
public abstract class VoltageSource extends AbstractSource {

	/**
	 * Creates a new voltage source
	 * @param first - the first point that this source is connected to
	 * @param second - the second point that this source is connected to
	 */
	public VoltageSource(ConnectionPoint first, ConnectionPoint second) {
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

}
