package xyz.kpzip.kspice.component.source.voltage;

import xyz.kpzip.kspice.circuit.Circuit;
import xyz.kpzip.kspice.component.source.AbstractSource;
import xyz.kpzip.kspice.util.math.Function;

public class ImmutableFunctionSource extends AbstractSource {
	
	private double t;
	private final Function fn;
	private final double timeOffset;

	public ImmutableFunctionSource(Circuit.ConnectionPoint n1, Circuit.ConnectionPoint n2, final Function fn) {
		super(n1, n2);
		this.fn = fn;
		t = 0;
		timeOffset = 0;
	}
	
	public ImmutableFunctionSource(Circuit.ConnectionPoint n1, Circuit.ConnectionPoint n2, final Function fn, final double timeOffset) {
		super(n1, n2);
		this.fn = fn;
		t = timeOffset;
		this.timeOffset = timeOffset;
	}
	
	public Function getFunction() {
		return fn;
	}
	
	public double getTimeOffset() {
		return timeOffset;
	}

	@Override
	public double getSourceVoltage() {
		return fn.sample(t);
	}
	
	@Override
	public void updateTime(double time) {
		t += time;
	}
	
	@Override
	public void reset() {
		t = timeOffset;
	}

}
