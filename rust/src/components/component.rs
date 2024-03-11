use std::sync::Arc;
use crate::circuit::{Circuit, ConnectionPoint};

//Requires send and sync since components need to be shared amongst threads
pub trait Component<'a>: Send + Sync {
    fn connection_point_count(&self) -> u32;

    fn connection_count(&self) -> u32;

    fn connections(&self) -> [(&'a ConnectionPoint<'a>, &'a ConnectionPoint<'a>, &dyn Component)];

    fn constraints(&self) -> [f64];

    fn update_current(&mut self, currents: Vec<f64>);

    fn differential(&mut self, _dt: f64) {}

    fn reset(&mut self);

    fn put(self, circuit: &mut Arc<Circuit>) {
        circuit.add_component(Box::new(self));
    }
}
