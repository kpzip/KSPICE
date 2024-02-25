package xyz.kpzip.kspice.util.math;

/**
 * Represents a Mathematical Function.
 * 
 * @author kpzip
 *
 */
@FunctionalInterface
public interface Function {
	
	/**
	 * Gets the value of the function at the specified input.
	 * Essentially computes f(x)
	 * 
	 * @param x - the input to the function
	 * @return - the value of the function at the input point x.
	 */
	double sample(double x);
	
	/**
	 * Creates a function that represents the composition of two other functions.
	 * f(g(x))
	 * 
	 * @param f - the outter function for composition
	 * @param g - the innter function for composition
	 * @return - the composed function f(g(x))
	 */
	public static Function compose(Function f, Function g) {
		return (x) -> f.sample(g.sample(x));
	}

}
