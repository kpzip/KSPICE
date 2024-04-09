package xyz.kpzip.kspice.component.passive;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.Abstract2NodeComponent;

/**
 * Represents a capacitor component. Stores capacitance and charge information.
 * Capacitance is immutable.
 * @author kpzip
 *
 */
public class Capacitor extends Abstract2NodeComponent {
	
	private final double capacitance;
	
	private double charge = 0.0;

	/**
	 * Create a new capacitor, connecting it to connection points.
	 * Capacitance is immutable.
	 * @param first - the first connection point to connect this capacitor to
	 * @param second - the second connection point to connect this capacitor to
	 * @param capacitance - the capacitance of this capacitor in Farads
	 */
	public Capacitor(Circuit.ConnectionPoint first, Circuit.ConnectionPoint second, final double capacitance) {
		super(first, second);
		this.capacitance = capacitance;
	}

	/**
	 * @implNote capacitors follow the equation Q = CV (Integral form of I = c(dv/dt)). This equation is also equivalent to Q = CV + 0 * I, meaning that the current dependence is always zero.
	 */
	@Override
	public double currentDependence() {
		return 0;
	}

	/**
	 * @implNote capacitors follow the equation Q = CV, making the coefficient on V equal to C
	 */
	@Override
	public double voltageDependence() {
		return capacitance;
	}

	/**
	 * @implNote capacitors follow the equation Q = CV, making the constant dependence equal to Q.
	 */
	@Override
	public double constantDependence() {
		return charge;
	}
	
	/**
	 * gets this capacitor's capacitance in Farads
	 * @return - the capacitance of this capacitor in Farads
	 */
	public double getCapacitance() {
		return capacitance;
	}
	
	/**
	 * gets the current charge on the capacitor in Columbs
	 * @return - the charge on this capacitor in Columbs
	 */
	public double getCharge() {
		return charge;
	}
	
	/**
	 * @implNote updates the charge on this capacitor based on the current flowing into it.
	 * @implSpec this function is effectively an Euler approximation of the differential equation integral(I)dt = CV
	 */
	@Override
	public void differential(double dt) {
		this.charge += this.isReversed() ? this.current * dt : -this.current * dt;
	}
	
	/**
	 * @implNote Resets the charge on this capacitor since it varies with time.
	 */
	@Override
	public void reset() {
		this.charge = 0;
		super.reset();
	}

}
