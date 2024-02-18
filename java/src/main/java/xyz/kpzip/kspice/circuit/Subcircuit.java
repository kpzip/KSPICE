package xyz.kpzip.kspice.circuit;

import xyz.kpzip.kspice.circuit.CircuitUtil.ConnectionPointPair;
import xyz.kpzip.kspice.component.Component;
import xyz.kpzip.kspice.util.ArrayBuilder;

public non-sealed class Subcircuit extends Circuit implements Component {

	//Don't create a ground node
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
		ArrayBuilder<ConnectionPointPair> connections = new ArrayBuilder<ConnectionPointPair>(ConnectionPointPair.class);
		for (Component c : components) connections.pushArray(c.connections());
		return connections.toArray();
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
