package xyz.kpzip.kspice.components;

import java.util.function.BiFunction;

import xyz.kpzip.kspice.circuit.Circuit;

@FunctionalInterface
public interface BiNodeComponentSupplier<C extends Component> extends BiFunction<Circuit.ConnectionPoint, Circuit.ConnectionPoint, C> {

}
