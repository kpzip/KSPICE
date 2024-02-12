use super::super::circuit::ConnectionPoint;

pub trait Component<'a> {
    fn connection_point_count() -> u32;

    fn connection_count() -> u32;

    fn connections() -> Vec<(ConnectionPoint<'a>, ConnectionPoint<'a>)>;

    fn constraints() -> Vec<f64>;

    fn upade_current(currents: Vec<f64>);

    fn differential(_dt: f64) {}

    fn reset();
}
