package xyz.kpzip.kspice.component.source;

import xyz.kpzip.kspice.component.BiNodeComponentSupplier;
import xyz.kpzip.kspice.util.math.Function;

public final class Sources {

	private Sources() { throw new IllegalStateException("No Sources Insatances for YOU!");}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> sin(double amplitude, double period, double phaseShift, double verticalShift) {
		final double alpha = 2 * Math.PI / period;
		final double beta = phaseShift * alpha;
		Function gen = (x) -> (amplitude * Math.sin(alpha * x + beta) + verticalShift);
		return (n1, n2) -> new ImmutableFunctionSource(n1, n2, gen);
	}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> sin(double amplitude, double period, double phaseShift) {
		return sin(amplitude, period, phaseShift, 0);
	}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> sin(double amplitude, double period) {
		return sin(amplitude, period, 0, 0);
	}
	
	
	
	
	public static BiNodeComponentSupplier<? extends AbstractSource> square(double amplitude, double period, double dutyCycle, boolean startOn, double phaseShift, double verticalShift) {
		if (period == 0 || dutyCycle == 1 || dutyCycle == 0) return (n1, n2) -> new Battery(n1, n2, period == 0 && dutyCycle != 1 && dutyCycle != 0 ? verticalShift + (startOn ? 1 : 0) * amplitude : verticalShift + dutyCycle * amplitude);
		Function gen = (x) -> ((x - phaseShift) % period > dutyCycle) ^ !startOn ? verticalShift : verticalShift + amplitude;
		return (n1, n2) -> new ImmutableFunctionSource(n1, n2, gen);
	}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> square(double amplitude, double period, double dutyCycle, boolean startOn, double phaseShift) {
		return square(amplitude, period, dutyCycle, startOn, phaseShift, 0);
	}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> square(double amplitude, double period, double dutyCycle, boolean startOn) {
		return square(amplitude, period, dutyCycle, startOn, 0, 0);
	}
	
	public static BiNodeComponentSupplier<? extends AbstractSource> square(double amplitude, double period) {
		return square(amplitude, period, 0.5, false, 0, 0);
	}

}
