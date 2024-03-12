use crate::circuit::ConnectionPoint;
use crate::components::component::Component;

pub struct TwoNodeComponentBase<'a> {
    first: &'a ConnectionPoint,
    second: &'a ConnectionPoint,
    current: f64,
}

impl<'a> TwoNodeComponentBase<'a> {

    #[inline]
    pub const fn connection_point_count(&self) -> u32 {
        2
    }

    #[inline]
    pub const fn connection_count(&self) -> u32 {
        1
    }

    #[inline]
    pub const fn connections(&self) -> Box<[(&'a ConnectionPoint, &'a ConnectionPoint, &dyn Component)]> {
        Box::new([(self.first, self.second, self)])
    }

    #[inline]
    pub fn update_current(&mut self, currents: Vec<f64>) {
        self.current = currents.first().unwrap().clone();
    }

    #[inline]
    pub fn reset(&mut self) {
        self.current = 0.0;
    }

    pub fn new(first: &'a ConnectionPoint, second: &'a ConnectionPoint) -> TwoNodeComponentBase<'a> {
        TwoNodeComponentBase {
            first,
            second,
            current: 0.0,
        }
    }

    pub fn is_reversed(&self) -> bool {
        self.first > self.second
    }

    pub fn reversed_multiplier(&self) -> f64 {
        if self.is_reversed() {
            -1.0
        }
        1.0
    }

    pub const fn get_current(&self) -> f64 {
        self.current
    }
}