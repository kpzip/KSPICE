package xyz.kpzip.kspice.circuit;

import xyz.kpzip.kspice.components.Component;

public non-sealed class Subcircuit extends Circuit implements Component {

	public Subcircuit() {
		super(false);
	}

	@Override
	public int connectionPointCount() {
		return this.connectionPoints.size();
	}

	@Override
	public int connectionCount() {
		return this.components.stream().mapToInt((c) -> c.connectionCount()).sum();
	}

	@Override
	public ConnectionPointPair[] connections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] constraints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCurrent(double[] currents) {
		// TODO Auto-generated method stub
		
	}

}
