use crate::circuit::ConnectionPoint;
use crate::components::base::TwoNodeComponentBase;
use crate::components::component::Component;

pub struct Battery<'a> {
    base: TwoNodeComponentBase<'a>,
    emf: f64,
}

impl<'a> Component for Battery<'a> {
    fn connection_point_count(&self) -> u32 {
        self.base.connection_point_count()
    }

    fn connection_count(&self) -> u32 {
        self.base.connection_count()
    }

    fn connections(&self) -> [(&'a ConnectionPoint<'a>, &'a ConnectionPoint<'a>, &dyn Component); 1] {
        self.base.connections()
    }

    //Update this to negate emf in the constructor
    fn constraints(&self) -> [f64; 3] {
        [0.0, 1.0, self.base.reversed_multiplier() * self.emf]
    }

    fn update_current(&mut self, currents: Vec<f64>) {
        self.base.update_current(currents)
    }

    fn reset(&mut self) {
        self.base.reset()
    }
}

impl<'a> Battery<'a> {
    pub fn new(first: &'a ConnectionPoint<'a>, second: &'a ConnectionPoint<'a>, emf: f64) -> Battery {
        Battery {
            base: TwoNodeComponentBase::new(first, second),
            emf,
        }
    }

    pub const fn get_current(&self) -> f64 {
        self.base.get_current()
    }
}