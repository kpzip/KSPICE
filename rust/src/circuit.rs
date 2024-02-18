pub struct Circuit<'a> {
    connection_points: Vec<ConnectionPoint<'a>>,
    ground: Option<&'a ConnectionPoint<'a>>,
    connection_point_counter: u32,
}

impl<'a> Circuit<'a> {
    pub fn new() -> Circuit<'a> {
        let mut circuit: Circuit = Circuit {
            connection_point_counter: 0,
            connection_points: Vec::new(),
            ground: None,
        };

        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit: &mut circuit as *mut Circuit,
        };

        circuit.connection_points.push(ground);

        circuit
    }

    fn new_connection_point(&mut self) -> &'a ConnectionPoint<'a> {
        unsafe {
            self.connection_points.push(ConnectionPoint::new(self));
            self.connection_points.last().unwrap()
        }
    }
}

pub struct ConnectionPoint<'a> {
    id: u32,
    voltage: f64,
    circuit: *mut Circuit<'a>,
}

impl<'a> ConnectionPoint<'a> {
    unsafe fn new(circuit: &'a mut Circuit<'a>) -> ConnectionPoint<'a> {
        circuit.connection_point_counter = circuit.connection_point_counter + 1;
        ConnectionPoint {
            id: circuit.connection_point_counter,
            voltage: 0.0,
            circuit: circuit as *mut Circuit<'a>,
        }
    }

    unsafe fn get_circuit(&mut self) -> &mut Circuit<'a> {
        &mut *self.circuit
    }
}
