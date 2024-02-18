package xyz.kpzip.kspice.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.passive.Resistor;
import xyz.kpzip.kspice.component.semiconductor.AbstractTransistor;
import xyz.kpzip.kspice.component.semiconductor.TransistorType;
import xyz.kpzip.kspice.component.source.voltage.Battery;

final class CircuitTests {
	
	private CircuitTests() {}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitTests.class);

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
	
	@Test
	public void transistorTest() {
		Circuit c = new Circuit();
		Circuit.ConnectionPoint collector = c.createConnectionPoint();
		Circuit.ConnectionPoint base = c.createConnectionPoint();
		Circuit.ConnectionPoint emitter = c.getGround();
		Circuit.ConnectionPoint v = c.createConnectionPoint();
		Circuit.ConnectionPoint v2 = c.createConnectionPoint();
		Battery b1 = new Battery(emitter, v2, 0.7);
		Resistor r1 = new Resistor(v2, base, 100.0); 
		Resistor r2 = new Resistor(v, collector, 100.0);
		Battery b2 = new Battery(emitter, v, 10.0);
		AbstractTransistor q1 = new AbstractTransistor(collector, base, emitter, 10) {

			@Override
			public TransistorType getType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public double getEndCurrent() {
				return 0.05;
			}

			@Override
			public double IVslope() {
				return 1;
			}
			
		};
		c.addComponent(b1);
		c.addComponent(r1);
		c.addComponent(r2);
		c.addComponent(b2);
		c.addComponent(q1);
		c.simulationStep(0.001);
		LOGGER.info(() -> c.toString());
		c.simulationStep(0.001);
		LOGGER.info(() -> c.toString());
		LOGGER.info(() -> String.valueOf(r1.getCurrent()));
		assertTrue(Math.abs(r1.getCurrent()) > 0.001);
	}

}
