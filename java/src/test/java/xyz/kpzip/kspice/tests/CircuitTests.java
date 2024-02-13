package xyz.kpzip.kspice.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.passive.Resistor;
import xyz.kpzip.kspice.component.source.Battery;

final class CircuitTests {
	
	private CircuitTests() {}

	@Test
	public void lightTest() {
		Circuit c = new Circuit();
    	Circuit.ConnectionPoint v = c.createConnectionPoint();
    	Battery b = new Battery(c.getGround(), v, 10.0);
    	Resistor r = new Resistor(v, c.getGround(), 10.0);
    	c.addComponent(b);
    	c.addComponent(r);
    	c.simulationStep(0);
    	assertEquals(1.0, r.getCurrent());
	}

}
