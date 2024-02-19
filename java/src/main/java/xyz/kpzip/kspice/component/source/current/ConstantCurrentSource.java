/**
 * 
 */
package xyz.kpzip.kspice.component.source.current;

import xyz.kpzip.kspice.circuit.Circuit;

/**
 * 
 * Represents a source that always has a specified current value.<br>
 * This current source is immutable.
 * 
 * @author kpzip
 * 
 */
public class ConstantCurrentSource extends CurrentSource {

	private final double currentValue;
	
	/**
	 * Creates a new Constant current source.
	 * @param first - the first connection point to connect this source to
	 * @param second - the second connection point to connect this source to
	 * @param value - the current that this source will generate in Amperes.
	 */
	public ConstantCurrentSource(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second, final double value) {
		super(first, second);
		this.currentValue = value;
	}

	@Override
	public double getSourceValue() {
		return currentValue;
	}
	
	/**
	 * gets the value of the current that this source produces in Amps.
	 * @return the currentValue
	 */
	public double getCurrentValue() {
		return currentValue;
	}



}
