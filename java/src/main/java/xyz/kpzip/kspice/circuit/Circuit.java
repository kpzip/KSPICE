package xyz.kpzip.kspice.circuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import xyz.kpzip.kspice.component.Component;
import xyz.kpzip.kspice.util.ArrayBuilder;

/**
 * Represents an electronic circuit.
 * Components can be added with addComponent(Component c), and new connection points can be made with createConnectionPoint().
 * To simulate, call simulationStep(double dt)
 * 
 * @author kpzip
 *
 */
public sealed class Circuit permits Subcircuit {
	
	public static boolean DEBUG = false;
	
	//Nodes should always be in ascending order with no gaps e.g. 0,1,2,3,4,5 NOT 0,2,3,5,6,7,9
	//Positive Current is determined to flow from high id nodes to low id nodes
	protected TreeSet<ConnectionPoint> connectionPoints;
	protected ArrayList<Component> components;
	private ConnectionPoint ground;
	private int connectionPointIndex = 1;
	
	public Circuit() {
		this(true);
	}
	
	//Constructor to allow 
	protected Circuit(boolean gnd) {
		connectionPoints = new TreeSet<ConnectionPoint>();
		components = new ArrayList<Component>();
		if (gnd) {
			ground = new Circuit.ConnectionPoint(0);
			connectionPoints.add(ground);
		}
		else {
			ground = null;
		}
	}
	
	public void simulationStep(double dt) {
		
		final int numVoltages = connectionPoints.size();
		final int numCurrents = components.stream().mapToInt((c) -> c.connectionCount()).sum();
		final int numToSolveFor = numVoltages + numCurrents;
		
		if (DEBUG) {
			System.out.println("Voltages: " + numVoltages);
			System.out.println("Currents: " + numCurrents);
		}
		
		//The matrix representing the coefficients in the system
		double [] [] matrix = new double[numToSolveFor] [numToSolveFor];
		
		//The vector representing the vector (The constants in each equation)
		double [] constants = new double[numToSolveFor];
		
		//enter values for component constraints
		int componentConnectionIndex = 0;

		for (Component c : components) {
			
			//Unpack data
			ConnectionPointPair[] connections = c.connections();
			double[] constraints = c.constraints();
			int currConnection = 0;
			
			//Create an equation for each connection point
			for (int i = 0; i < connections.length; i++) {
				
				double[] coefficients = new double[numToSolveFor];
				
				int currentDependenceIndex = (2 * c.connectionCount() + 1) * i;
				int voltageDependenceIndex = currentDependenceIndex + c.connectionCount();
				double constantDependence = constraints[/* 3*i +  ?????? */ 2 * c.connectionCount() + i * (2 * c.connectionCount() + 1)];
				
				//Add all of the constraints to the equation
				//Add dependents on each connection's current and voltage
				for (int j = 0; j < connections.length; j++) {
					ConnectionPointPair connection = connections[j];
					
					//set current data
					coefficients[(componentConnectionIndex - currConnection) + j] = constraints[currentDependenceIndex + j];
					
					//Set it so the voltage across is equal to the voltage of the first one minus the voltage of the second one
					coefficients[numCurrents + connection.first.id] += constraints[voltageDependenceIndex + j];
					coefficients[numCurrents + connection.second.id] += -constraints[voltageDependenceIndex + j];
				}
				
				constants[componentConnectionIndex] = constantDependence;
				matrix[componentConnectionIndex] = coefficients;
				
				componentConnectionIndex++;
				currConnection++;
			}
		}
		
		//enter values for Kirchhoff's junction laws
		int nodeIndex = numCurrents;
		for (Circuit.ConnectionPoint p : connectionPoints) {
			
			double[] coefficients = new double[numToSolveFor];
			
			if (p == this.ground) {
				//add equation that says ground node is at zero voltage
				coefficients[numCurrents] = 1;
			}
			else {
				ArrayBuilder<ConnectionPointPair> arr = new ArrayBuilder<ConnectionPointPair>(ConnectionPointPair.class, numCurrents);
				components.forEach((c) -> arr.pushArray(c.connections()));
			
				//ordered array of all connections in the circuit
				ConnectionPointPair[] allConnections = arr.toArray();
			
				//make it so all of the currents flowing into the node sum up to zero
				//Note: current flows from high node values to low node values
				for (int i = 0; i < allConnections.length; i++) {
					if (allConnections[i].first == p) {
						coefficients[i] = -1;
						continue;
					}
					if (allConnections[i].second == p) {
						coefficients[i] = 1;
					}
				}
			}
			matrix[nodeIndex] = coefficients;
			nodeIndex++;
			
		}
		
		//Debug: print out matrix and vector
		if (DEBUG) {
			System.out.println(Arrays.deepToString(matrix));
			System.out.println(Arrays.toString(constants));
		}
		
		//Now the matrix is populated, time to solve!
		RealMatrix A = new Array2DRowRealMatrix(matrix, false);
		RealVector B = new ArrayRealVector(constants, false);
		DecompositionSolver solver = new LUDecomposition(A).getSolver();
		RealVector X = solver.solve(B);
		double[] solutions = X.toArray();
		
		//set current and voltage values
		int valPtr = 0;
		for (Component c : components) {
			c.updateCurrent(Arrays.copyOfRange(solutions, valPtr, valPtr += c.connectionCount()));
		}
		
		for (ConnectionPoint p : connectionPoints) {
			p.voltage = solutions[valPtr];
			valPtr++;
		}
		components.forEach((c) -> c.differential(dt));
	}
	
	public void addComponent(Component c) {
		components.add(c);
	}
	
	public Circuit.ConnectionPoint createConnectionPoint() {
		ConnectionPoint c = new Circuit.ConnectionPoint();
		connectionPoints.add(c);
		return c;
	}
	
	//May return null
	public ConnectionPoint getGround() {
		return ground;
	}
	
	public void reset() {
		connectionPoints.forEach((p) -> p.reset());
		components.forEach((c) -> c.reset());
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Circuit.ConnectionPoint c : connectionPoints) {
			str.append(c.toString());
			str.append("\n");
		}
		return str.toString();
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(components, connectionPoints);
	}

	public void validateConnectionPoints() {
		int connectionPointCheckerIndex = -1;
		for (Circuit.ConnectionPoint c : connectionPoints) {
			if (c.id != ++connectionPointCheckerIndex) {
				c.id = connectionPointCheckerIndex;
			}
		}
		connectionPointIndex = ++connectionPointCheckerIndex;
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public TreeSet<ConnectionPoint> getConnectionPoints() {
		return connectionPoints;
	}
	
	public class ConnectionPoint implements Comparable<ConnectionPoint> {
		
		//id 0 is ground, negative is not part of the circuit
		private int id;
		
		private double voltage = 0;
		
		//Only used for creating the ground node
		protected ConnectionPoint(int id) {
			this.id = id;
		}
		
		protected ConnectionPoint() {
			this.id = connectionPointIndex++;
		}
		
		public double getVoltage() {
			return voltage;
		}

		public void setVoltage(double voltage) {
			this.voltage = voltage;
		}
		
		public int getId() {
			return id;
		}

		@Override
		public int compareTo(ConnectionPoint o) {
			return this.id - o.id;
		}
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(id);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConnectionPoint other = (ConnectionPoint) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return id == other.id;
		}

		public String toString() {
			return "Node: " + id + " with voltage: " + voltage + "V" + (id == 0 ? " (GND)" : "");
		}

		private Circuit getEnclosingInstance() {
			return Circuit.this;
		}
		
		public void reset() {
			this.voltage = 0;
		}

	}

}
