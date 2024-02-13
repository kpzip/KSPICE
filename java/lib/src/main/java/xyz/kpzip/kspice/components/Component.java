package xyz.kpzip.kspice.components;

import xyz.kpzip.kspice.circuit.ConnectionPointPair;

/**
 * Represents a component in a logical circuit designed for constraint solving.
 * Implementations are required to store a reference to each connection point they connect to, and a current value for each internal path.
 * @see Resistor
 * @see Capacitor
 * @see Abstract2NodeComponent
 * @author kpzip
 *
 */
public interface Component {
	
	/**
	 * gets the number of connection points a.k.a pins of this component.
	 * e.g. a resistor or battery would return 2, while a transistor would return 3.
	 * @return the number of connection points of this component
	 */
	int connectionPointCount();
	
	/**
	 * gets the number of ways current can flow through this component. Usually equal to connectionPointCount() - 1.
	 * e.g. a resistor or battery would return 1, while a transistor would return 2 
	 * (since transistors have base emitter current as well as collector emitter current).
	 * @return the number of connections this component has
	 */
	int connectionCount();
	
	/**
	 * gets the various connections in this component.
	 * e.g. a resistor would return an array of length 1 containing both pins as a pair, while a transistor would return
	 * an array of length 2 containing the base and the emitter as a pair as well as the collector and the emitter as a pair.
	 * @return the connections inside of this component. Length must be equal to the return value of connectionCount()
	 */
	ConnectionPointPair[] connections();
	
	//current dependence, voltage dependence, and constant dependence for each connection
	/**
	 * gets the mathematical equations this component obeys.
	 * the returned value should contain, in order the current dependences of the first equation in order of the connections returned by
	 * connections(), the voltage difference dependencies in order of the connections returned by connections(), and the constant dependence for each equation.
	 * the number of equations must be equal to the return value of connectionCount()
	 * @return the coefficients used to solve for this component
	 */
	double[] constraints();
	
	/**
	 * should update this components stored value for its current. Called after every simulation step.
	 * @param currents - the currents of each connection, in the order they are put in connections(). Length is guaranteed to be equal to the value of connectionCount()
	 */
	void updateCurrent(double[] currents);
	
	/**
	 * Used to update this component's differential values, for those which use an Euler approximation.
	 * @see Capacitor
	 * @see Inductor
	 * @param dt - the time elapsed in the previous simulation step.
	 */
	default void differential(double dt) {};

	/**
	 * Resets this component. Should set the current to 0 and resent any time varying values to their defaults.
	 */
	void reset();
}
