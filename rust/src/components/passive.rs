use std::sync::Arc;
use crate::circuit::ConnectionPoint;
use crate::components::base::TwoNodeComponentBase;
use crate::components::component::Component;

pub struct Resistor {
    base: TwoNodeComponentBase,
    resistance: f64,
}

impl Component for Resistor {
    fn connection_point_count(&self) -> usize {
        self.base.connection_count()
    }

    fn connection_count(&self) -> usize {
        self.base.connection_point_count()
    }

    fn connections<'b>(&'b self) -> Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'b dyn Component)]> {
        self.base.connections(self)
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

impl Resistor {
    pub fn new(first: &Arc<ConnectionPoint>, second: &Arc<ConnectionPoint>, resistance: f64) -> Resistor {
        Resistor {
            base: TwoNodeComponentBase::new(first, second),
            resistance,
        }
    }

    pub const fn get_current(&self) -> f64 {
        self.base.get_current()
    }
}

pub struct Capacitor {
    base: TwoNodeComponentBase,
    capacitance: f64,
    charge: f64,
}

impl Component for Capacitor {
    fn connection_point_count(&self) -> usize {
        self.base.connection_count()
    }

    fn connection_count(&self) -> usize {
        self.base.connection_point_count()
    }

    fn connections<'b>(&'b self) -> Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'b dyn Component)]> {
        self.base.connections(self)
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

impl Capacitor {
    pub fn new(first: &Arc<ConnectionPoint>, second: &Arc<ConnectionPoint>, capacitance: f64) -> Capacitor {
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