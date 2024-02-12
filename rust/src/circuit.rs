pub struct Circuit<'a> {
    connection_points: Vec<ConnectionPoint<'a>>,
    ground: Option<&'a ConnectionPoint<'a>>,
    connection_point_counter: u32,
}

impl<'a> Circuit<'a> {
    pub fn new() -> Circuit<'a> {
        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit: None,
        };

        let mut circuit: Circuit = Circuit {
            connection_point_counter: 0,
            connection_points: Vec::new(),
            ground: None,
        };

        circuit.connection_points.push(ground);

        circuit.ground = circuit.connection_points.first();

        circuit
    }
}

pub struct ConnectionPoint<'a> {
    id: u32,
    voltage: f64,
    circuit: Option<&'a Circuit<'a>>,
}

impl<'a> ConnectionPoint<'a> {
    fn new(circuit: &'a Circuit) -> ConnectionPoint<'a> {
        ConnectionPoint {
            id: circuit.connection_point_counter,
            voltage: 0.0,
            circuit: Some(circuit),
        }
    }
}
