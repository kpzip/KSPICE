package xyz.kpzip.kspice.components;

import xyz.kpzip.kspice.circuit.ConnectionPointPair;

public interface Component {
	
	int connectionPointCount();
	
	int connectionCount();
	
	ConnectionPointPair[] connections();
	
	//current dependence, voltage dependence, and constant dependence for each connection
	double[] constraints();
	
	void updateCurrent(double[] currents);
	
	default void differential(double dt) {};

	void reset();
}
