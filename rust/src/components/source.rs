use std::sync::Arc;
use crate::circuit::ConnectionPoint;
use crate::components::base::TwoNodeComponentBase;
use crate::components::component::Component;

pub struct Battery {
    base: TwoNodeComponentBase,
    emf: f64,
}

impl Component for Battery {
    fn connection_point_count(&self) -> usize {
        self.base.connection_point_count()
    }

    fn connection_count(&self) -> usize {
        self.base.connection_count()
    }

    fn connections<'b>(&'b self) -> Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'b dyn Component)]> {
        self.base.connections(self)
    }

    //Update this to negate emf in the constructor
    fn constraints(&self) -> Box<[f64]> {
        Box::new([0.0, 1.0, self.base.reversed_multiplier() * self.emf])
    }

    fn update_current(&mut self, currents: Vec<f64>) {
        self.base.update_current(currents)
    }

    fn reset(&mut self) {
        self.base.reset()
    }
}

impl Battery {
    pub fn new(first: &Arc<ConnectionPoint>, second: &Arc<ConnectionPoint>, emf: f64) -> Battery {
        Battery {
            base: TwoNodeComponentBase::new(first, second),
            emf,
        }
    }

    pub const fn get_current(&self) -> f64 {
        self.base.get_current()
    }
}