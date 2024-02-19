use super::components::component::Component;
use std::collections::BTreeSet;
use std::sync::Arc;
use std::sync::Mutex;

pub struct Circuit<'a> {
    connection_points: Mutex<BTreeSet<ConnectionPoint<'a>>>,
    components: Mutex<Vec<Box<dyn Component<'a>>>>,
    ground: Option<&'a ConnectionPoint<'a>>,
    connection_point_counter: Mutex<u32>,
}

impl<'a> Circuit<'a> {
    pub fn new() -> Arc<Circuit<'a>> {
        let circuit: Arc<Circuit> = Arc::new(Circuit {
            connection_point_counter: Mutex::new(0),
            components: Mutex::new(Vec::new()),
            connection_points: Mutex::new(BTreeSet::new()),
            ground: None,
        });

        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit: circuit.clone(),
        };

        circuit.connection_points.lock().unwrap().insert(ground);

        circuit
    }

    fn new_connection_point(circuit: &'a Arc<Circuit<'a>>) -> &'a ConnectionPoint<'a> {
        circuit
            .connection_points
            .lock()
            .unwrap()
            .insert(ConnectionPoint::new(circuit.clone()));
        //unsafe block here bypasses the Mutex on connection_points. This is fine since this is a
        //read only reference and it is guaranteed to not live longer than at least one Arc<Circuit>
        unsafe {
            &*(circuit.connection_points.lock().unwrap().last().unwrap()
                as *const ConnectionPoint<'a>)
        }
    }
}

pub struct ConnectionPoint<'a> {
    id: u32,
    voltage: f64,
    circuit: Arc<Circuit<'a>>,
}

impl<'a> ConnectionPoint<'a> {
    fn new(circuit: Arc<Circuit<'a>>) -> ConnectionPoint<'a> {
        *circuit.connection_point_counter.lock().unwrap() += 1;
        ConnectionPoint {
            id: *circuit.connection_point_counter.lock().unwrap(),
            voltage: 0.0,
            circuit: circuit.clone(),
        }
    }

    fn get_circuit(&self) -> Arc<Circuit<'a>> {
        self.circuit.clone()
    }
}

impl PartialEq for ConnectionPoint<'_> {
    fn eq(&self, other: &Self) -> bool {
        self.id == other.id
            && self.circuit.as_ref() as *const Circuit<'_>
                == other.circuit.as_ref() as *const Circuit<'_>
    }
}
