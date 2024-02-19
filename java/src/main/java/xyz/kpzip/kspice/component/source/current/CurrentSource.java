/**
 * 
 */
package xyz.kpzip.kspice.component.source.current;

import xyz.kpzip.kspice.circuit.Circuit.ConnectionPoint;
import xyz.kpzip.kspice.component.source.AbstractSource;

/**
 * 
 * @author kpzip
 * 
 */
public abstract class CurrentSource extends AbstractSource {

	/**
	 * Creates a new current source.
	 * @param first - the first point that this source is connected to
	 * @param second - the second point that this source is connected to
	 */
	public CurrentSource(ConnectionPoint first, ConnectionPoint second) {
		super(first, second);
	}

	@Override
	public double currentDependence() {
		return 1.0;
	}

	@Override
	public double voltageDependence() {
		return 0;
	}

}
