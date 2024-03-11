mod circuit;
mod components;
mod util;

#[cfg(test)]
mod tests {
    use std::sync::Arc;
    use crate::circuit::{Circuit, ConnectionPoint};
    use crate::components::component::Component;
    use crate::components::passive::Resistor;
    use crate::components::source::Battery;

    #[test]
    fn resistor_battery_test() {
        let mut c: Arc<Circuit> = Circuit::new();
        let gnd: &ConnectionPoint = c.get_ground();
        let con1: &ConnectionPoint = Circuit::new_connection_point(&c);

        let resistor: Resistor = Resistor::new(gnd, con1, 10.0);
        let res = &resistor;
        let battery: Battery = Battery::new(gnd, con1, 10.0);

        resistor.put(&mut c);
        battery.put(&mut c);

        c.simulation_step(0.1);

        assert_eq!(res.get_current(), 1.0);
    }
}
