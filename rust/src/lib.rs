pub mod circuit;
pub mod components;
mod util;

#[cfg(test)]
mod tests {
    use crate::circuit::Circuit;
    use crate::components::passive::Resistor;
    use crate::components::source::Battery;

    #[test]
    fn resistor_battery_test() {
        let mut c: Circuit = Circuit::new();
        let gnd = c.get_ground();
        let con1 = c.new_connection_point();

        let resistor: Resistor = Resistor::new(&gnd, &con1, 10.0);
        //let res = &resistor;
        let battery: Battery = Battery::new(&gnd, &con1, 10.0);

        c.add_component(resistor);

        c.add_component(battery);

        c.simulation_step(0.1);

        //assert_eq!(res.get_current(), 1.0);
    }
}
