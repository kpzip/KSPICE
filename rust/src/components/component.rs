use crate::circuit::ConnectionPoint;

//Requires send and sync since components need to be shared amongst threads
pub trait Component<'a>: Send + Sync {
    fn connection_point_count(&self) -> u32;

    fn connection_count(&self) -> u32;

    fn connections(&self) -> Box<[(&'a ConnectionPoint, &'a ConnectionPoint, &dyn Component)]>;

    fn constraints(&self) -> Box<[f64]>;

    fn update_current(&mut self, currents: Vec<f64>);

    fn differential(&mut self, _dt: f64) {}

    fn reset(&mut self);

}
