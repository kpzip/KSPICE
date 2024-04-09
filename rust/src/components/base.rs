use std::sync::Arc;
use crate::circuit::ConnectionPoint;
use crate::components::component::Component;

pub struct TwoNodeComponentBase {
    first: Arc<ConnectionPoint>,
    second: Arc<ConnectionPoint>,
    current: f64,
}

impl TwoNodeComponentBase {

    #[inline]
    pub const fn connection_point_count(&self) -> usize {
        2
    }

    #[inline]
    pub const fn connection_count(&self) -> usize {
        1
    }



    #[inline]
    pub fn update_current(&mut self, currents: Vec<f64>) {
        self.current = currents.first().unwrap().clone();
    }

    #[inline]
    pub fn reset(&mut self) {
        self.current = 0.0;
    }

    pub fn new(first: &Arc<ConnectionPoint>, second: &Arc<ConnectionPoint>) -> TwoNodeComponentBase {
        TwoNodeComponentBase {
            first: first.clone(),
            second: second.clone(),
            current: 0.0,
        }
    }

    pub fn is_reversed(&self) -> bool {
        self.first > self.second
    }

    pub fn reversed_multiplier(&self) -> f64 {
        if self.is_reversed() {
            return -1.0;
        }
        1.0
    }

    pub const fn get_current(&self) -> f64 {
        self.current
    }
}

impl TwoNodeComponentBase {
    #[inline]
    pub fn connections<'b>(&'b self, component: &'b (dyn Component + 'b)) -> Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'b dyn Component)]> {
        Box::new([(self.first.clone(), self.second.clone(), component)])
    }
}