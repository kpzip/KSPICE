/**
 * 
 */
package xyz.kpzip.kspice.component.passive;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.Abstract2NodeComponent;
import xyz.kpzip.kspice.optimizer.OptimizeableComponent;

/**
 * 
 * Represents an ideal wire with zero internal resistance.<br><br>
 * Usually, this component should not be used due to the fact that both connection points can be merged to achieve the same circuit. Some circuits may optimize this component out.
 * 
 * @author kpzip
 * 
 */
public class Wire extends Abstract2NodeComponent implements OptimizeableComponent {

	/**
	 * Creates a new wire that connects two points in the circuit.
	 */
	public Wire(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second) {
		super(first, second);
	}

	@Override
	public double currentDependence() {
		return 0;
	}

	@Override
	public double voltageDependence() {
		return 1.0;
	}

	@Override
	public double constantDependence() {
		return 0;
	}

}
