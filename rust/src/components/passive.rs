use crate::circuit::ConnectionPoint;
use crate::components::base::TwoNodeComponentBase;
use crate::components::component::Component;

pub struct Resistor<'a> {
    base: TwoNodeComponentBase<'a>,
    resistance: f64,
}

impl<'a> Component<'a> for Resistor<'a> {
    fn connection_point_count(&self) -> u32 {
        self.base.connection_count()
    }

    fn connection_count(&self) -> u32 {
        self.base.connection_point_count()
    }

    fn connections(&self) -> Box<[(&'a ConnectionPoint, &'a ConnectionPoint, &dyn Component)]> {
        self.base.connections()
    }

    fn constraints(&self) -> Box<[f64]> {
        Box::new([self.resistance, -1.0, 0.0])
    }

    fn update_current(&mut self, currents: Vec<f64>) {
        self.base.update_current(currents)
    }

    fn reset(&mut self) {
        self.base.reset()
    }

}

impl<'a> Resistor<'a> {
    pub fn new(first: &'a ConnectionPoint, second: &'a ConnectionPoint, resistance: f64) -> Resistor<'a> {
        Resistor {
            base: TwoNodeComponentBase::new(first, second),
            resistance,
        }
    }

    pub const fn get_current(&self) -> f64 {
        self.base.get_current()
    }
}

pub struct Capacitor<'a> {
    base: TwoNodeComponentBase<'a>,
    capacitance: f64,
    charge: f64,
}

impl<'a> Component<'a> for Capacitor<'a> {
    fn connection_point_count(&self) -> u32 {
        self.base.connection_count()
    }

    fn connection_count(&self) -> u32 {
        self.base.connection_point_count()
    }

    fn connections(&self) -> Box<[(&'a ConnectionPoint, &'a ConnectionPoint, &dyn Component)]> {
        self.base.connections()
    }

    fn constraints(&self) -> Box<[f64]> {
        Box::new([0.0, self.capacitance, self.charge])
    }

    fn update_current(&mut self, currents: Vec<f64>) {
        self.base.update_current(currents)
    }

    fn differential(&mut self, _dt: f64) {
        self.charge += self.base.get_current() * self.base.reversed_multiplier();
    }

    fn reset(&mut self) {
        self.charge = 0.0;
        self.base.reset()
    }

}

impl<'a> Capacitor<'a> {
    pub fn new(first: &'a ConnectionPoint, second: &'a ConnectionPoint, capacitance: f64) -> Capacitor<'a> {
        Capacitor {
            base: TwoNodeComponentBase::new(first, second),
            capacitance,
            charge: 0.0,
        }
    }

    pub const fn get_current(&self) -> f64 {
        self.base.get_current()
    }
}