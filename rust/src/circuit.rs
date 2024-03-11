use std::cmp::Ordering;
use super::components::component::Component;
use std::collections::BTreeSet;
use std::sync::{Arc, MutexGuard};
use std::sync::Mutex;

static CIRCUIT_INDEX_COUNTER: Mutex<u64> = Mutex::new(0);

pub struct Circuit<'a> {
    connection_points: Mutex<BTreeSet<ConnectionPoint<'a>>>,
    components: Mutex<Vec<Box<dyn Component<'a>>>>,
    ground: Option<&'a ConnectionPoint<'a>>,
    connection_point_counter: Mutex<u64>,
    id: u64,
}

impl<'a> Circuit<'a> {
    pub fn new() -> Arc<Circuit<'a>> {
        let counter: MutexGuard<u64> = CIRCUIT_INDEX_COUNTER.lock().unwrap();

        let circuit: Arc<Circuit> = Arc::new(Circuit {
            connection_point_counter: Mutex::new(0),
            components: Mutex::new(Vec::new()),
            connection_points: Mutex::new(BTreeSet::new()),
            ground: None,
            id: *counter,
        });

        *counter += 1;

        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit: circuit.clone(),
        };

        circuit.connection_points.lock().unwrap().insert(ground);

        circuit
    }

    pub fn new_connection_point(circuit: &'a Arc<Circuit<'a>>) -> &'a ConnectionPoint<'a> {
        circuit
            .connection_points
            .lock()
            .unwrap()
            .insert(ConnectionPoint::new(circuit.clone()));
        //unsafe block here bypasses the Mutex on connection_points. This is fine since this is a
        //read only reference, and it is guaranteed to not live longer than at least one Arc<Circuit>
        /*unsafe {
            &*(circuit.connection_points.lock().unwrap().last().unwrap()
                as *const ConnectionPoint<'a>)
        }*/
        circuit.connection_points.lock().unwrap().last().take().unwrap()
    }

    pub fn get_ground(&self) -> &'a ConnectionPoint<'a> {
        self.ground.unwrap()
    }

    pub fn add_component(&mut self, component: Box<dyn Component>) {
        self.components.lock().unwrap().push(component);
    }

    pub fn simulation_step(&mut self, dt: f64) {

    }
}

pub struct ConnectionPoint<'a> {
    id: u64,
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

impl Eq for ConnectionPoint<'_> {}

impl PartialOrd for ConnectionPoint<'_> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        match self.circuit.id.cmp(&other.circuit.id) {
            Ordering::Equal => Some(self.id.cmp(&other.id)),
            Ordering::Greater => Some(Ordering::Greater),
            Ordering::Less => Some(Ordering::Less),
        }
    }
}

impl Ord for ConnectionPoint<'_> {
    fn cmp(&self, other: &Self) -> Ordering {
        self.partial_cmp(other).unwrap()
    }
}

//Designed to be thread safe, since simulation may want to be deferred to another thread
unsafe impl Send for Circuit<'_> {}
unsafe impl Sync for Circuit<'_> {}

unsafe impl Send for ConnectionPoint<'_> {}
unsafe impl Sync for ConnectionPoint<'_> {}


