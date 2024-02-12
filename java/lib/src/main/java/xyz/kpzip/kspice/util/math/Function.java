package xyz.kpzip.kspice.util.math;

@FunctionalInterface
public interface Function {
	
	double sample(double x);
	
	
	// f(g(x))
	public static Function compose(Function f, Function g) {
		return (x) -> f.sample(g.sample(x));
	}

}
