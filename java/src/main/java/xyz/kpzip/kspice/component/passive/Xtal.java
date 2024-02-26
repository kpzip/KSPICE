package xyz.kpzip.kspice.component.passive;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.circuit.Subcircuit;

/**
 * Represents a piezoelectric crystal. (commonly abbreviated xtal)
 * 
 * @author kpzip
 *
 */
public class Xtal extends Subcircuit {
	
	private Circuit.ConnectionPoint point1, point2;
	final double parallelCapacitance, seriesCapacitance, seriesInductance, seriesResistance;

	public Xtal(Circuit.ConnectionPoint point1, Circuit.ConnectionPoint point2, 
			final double parallelCapacitance, final double seriesCapacitance, final double seriesInductance, final double seriesResistance) {
		super();
		this.point1 = point1;
		this.point2 = point2;
		this.parallelCapacitance = parallelCapacitance;
		this.seriesCapacitance = seriesCapacitance;
		this.seriesInductance = seriesInductance;
		this.seriesResistance = seriesResistance;
		build();
	}

	@Override
	protected void build() {
		//Shunt Capacitor
		this.addComponent(new Capacitor(point1, point2, parallelCapacitance));
		
		Circuit.ConnectionPoint lc = createConnectionPoint();
		
		Circuit.ConnectionPoint rc = createConnectionPoint();
		
		this.addComponent(new Capacitor(lc, rc, seriesCapacitance));
		this.addComponent(new Resistor(rc, point2, seriesResistance));
		this.addComponent(new Inductor(point1, lc, seriesInductance));
		
	}

	public double getParallelCapacitance() {
		return parallelCapacitance;
	}

	public double getSeriesCapacitance() {
		return seriesCapacitance;
	}

	public double getSeriesInductance() {
		return seriesInductance;
	}

	public double getSeriesResistance() {
		return seriesResistance;
	}
	
}
