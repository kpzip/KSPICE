/**
 * 
 */
package xyz.kpzip.kspice.component.source.current;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.Abstract2NodeComponent;

/**
 * 
 * Represents a source that always has a specified current value.<br>
 * This current source is immutable.
 * 
 * @author kpzip
 * TODO
 */
public class ConstantCurrentSource extends Abstract2NodeComponent {

	/**
	 * 
	 */
	public ConstantCurrentSource(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		super(first, second);
	}

	@Override
	public double currentDependence() {
		return 1.0d;
	}

	@Override
	public double voltageDependence() {
		return 0;
	}

	@Override
	public double constantDependence() {

		return 0;
	}

}
