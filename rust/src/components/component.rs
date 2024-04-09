use std::sync::Arc;
use crate::circuit::ConnectionPoint;

//Requires send and sync since components need to be shared amongst threads
pub trait Component: Send + Sync {
    fn connection_point_count(&self) -> usize;

    fn connection_count(&self) -> usize;

    fn connections<'b>(&'b self) -> Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'b dyn Component)]>;

    fn constraints(&self) -> Box<[f64]>;

    fn update_current(&mut self, currents: Vec<f64>);

    fn differential(&mut self, _dt: f64) {}

    fn reset(&mut self);

}
