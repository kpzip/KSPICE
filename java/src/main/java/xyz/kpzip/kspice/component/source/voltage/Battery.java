package xyz.kpzip.kspice.component.source.voltage;

import xyz.kpzip.kspice.circuit.Circuit;

public class Battery extends VoltageSource {

	private final double emf;
	
	public Battery(Circuit.ConnectionPoint annode, Circuit.ConnectionPoint cathode, final double emf) {
		super(annode, cathode);
		this.emf = emf;
	}
	
	public double getEmf() {
		return emf;
	}
	
	@Override
	public double getSourceValue() {
		return emf;
	}
	
}
