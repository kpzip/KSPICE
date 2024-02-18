package xyz.kpzip.kspice.component.source.voltage;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.source.AbstractSource;

public class Battery extends AbstractSource {

	private final double emf;
	
	public Battery(Circuit.ConnectionPoint annode, Circuit.ConnectionPoint cathode, final double emf) {
		super(annode, cathode);
		this.emf = emf;
	}
	
	public double getEmf() {
		return emf;
	}
	
	@Override
	public double getSourceVoltage() {
		return emf;
	}
	
}
