package xyz.kpzip.kspice.circuit;

import java.util.Arrays;

import xyz.kpzip.kspice.component.Component;
import xyz.kpzip.kspice.util.ArrayBuilder;
import xyz.kpzip.kspice.util.ArrayUtil;
import xyz.kpzip.kspice.util.CircuitUtil.ConnectionPointPair;

public non-sealed class Subcircuit extends Circuit implements Component {

	//Don't create a ground node
	public Subcircuit() {
		super(false);
	}

	@Override
	public final int connectionPointCount() {
		return this.connectionPoints.size();
	}

	@Override
	public final int connectionCount() {
		return this.components.stream().mapToInt((c) -> c.connectionCount()).sum();
	}

	/**
	 * @implNote this method MUST return the connections in the same order as the components iterator in order for constraints() to work.
	 */
	@Override
	public final ConnectionPointPair[] connections() {
		ArrayBuilder<ConnectionPointPair> connections = new ArrayBuilder<ConnectionPointPair>(ConnectionPointPair.class);
		for (Component c : components) connections.pushArray(c.connections());
		return connections.toArray();
	}

	/**
	 * @implNote this method makes using subcircuits somewhat expensive with all of the array allocation that we have to do. Maybe consider rewriting this one in c?
	 */
	@Override
	public final double[] constraints() {
		int newlen = connectionCount() * 2 + 1;
		double[] constraints = new double[connectionCount() * newlen];
		int equationindex = 0;
		ConnectionPointPair[] myConnections = connections();
		for (Component c : components) {
			ConnectionPointPair[] connections = c.connections();
			double[] equations = c.constraints();
			for (int i = 0; i < c.connectionCount(); i++) {
				int numconnections = c.connectionCount();
				int len = numconnections * 2 + 1;
				double[] equation = Arrays.copyOfRange(equations, i * len, (i + 1) * len);
				double[] bigequation = new double[newlen];

				
				//Copy over constant value
				bigequation[newlen - 1] = equation[len - 1];
				
				//Copy over voltage and current values
				int beginconnectionIndex = ArrayUtil.linearSearchEq(connections[0], myConnections);
				
				//Currents
				System.arraycopy(equation, 0, bigequation, beginconnectionIndex, numconnections);
				
				//Voltages
				System.arraycopy(equation, numconnections, bigequation, connectionCount() + beginconnectionIndex, numconnections);
				
				//Copy Array into return value
				System.arraycopy(bigequation, 0, constraints, equationindex * newlen, newlen);
				
				equationindex++;
			}
			
		}
		return constraints;
	}

	@Override
	public final void updateCurrent(double[] currents) {
		//set current and voltage values
		int valPtr = 0;
		for (Component c : components) {
			c.updateCurrent(Arrays.copyOfRange(currents, valPtr, valPtr += c.connectionCount()));
		}
	}

}
