use super::super::circuit::ConnectionPoint;

pub trait Component<'a> {
    fn connection_point_count(&self) -> u32;

    fn connection_count(&self) -> u32;

    fn connections(&self) -> Vec<(&'a ConnectionPoint<'a>, &'a ConnectionPoint<'a>)>;

    fn constraints(&self) -> Vec<f64>;

    fn upade_current(&mut self, currents: Vec<f64>);

    fn differential(&mut self, _dt: f64) {}

    fn reset(&mut self);
}
