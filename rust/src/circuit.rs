use std::cmp::Ordering;
use super::components::component::Component;
use std::collections::BTreeSet;
use std::sync::MutexGuard;
use std::sync::Mutex;

static CIRCUIT_INDEX_COUNTER: Mutex<u64> = Mutex::new(0);

pub struct Circuit<'a> {
    connection_points: Mutex<BTreeSet<ConnectionPoint>>,
    components: Mutex<Vec<Box<dyn Component<'a>>>>,
    ground: Option<&'a ConnectionPoint>,
    connection_point_counter: Mutex<u64>,
    id: u64,
}

impl<'a> Circuit<'a> {
    pub fn new() -> Circuit<'a> {
        let counter: MutexGuard<u64> = CIRCUIT_INDEX_COUNTER.lock().unwrap();

        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit_id: *counter,
        };

        let circuit: Circuit = Circuit {
            connection_point_counter: Mutex::new(0),
            components: Mutex::new(Vec::new()),
            connection_points: Mutex::new(BTreeSet::new()),
            ground: Some(&ground),
            id: *counter,
        };

        *counter += 1;

        circuit.connection_points.lock().unwrap().insert(ground);

        circuit
    }

    pub fn new_connection_point(&mut self) -> &'a ConnectionPoint {
        self
            .connection_points
            .lock()
            .unwrap()
            .insert(ConnectionPoint::new(self));
        self.connection_points.lock().unwrap().last().take().unwrap()
    }

    pub fn get_ground(&self) -> &ConnectionPoint {
        self.ground.unwrap()
    }

    pub fn add_component<'b>(&mut self, component: impl Component<'b>) {
        self.components.lock().unwrap().push(Box::new(component));
    }

    pub fn simulation_step(&mut self, dt: f64) {

    }
}

pub struct ConnectionPoint {
    id: u64,
    voltage: f64,
    circuit_id: u64,
}

impl ConnectionPoint {
    fn new(circuit: &Circuit) -> ConnectionPoint {
        *circuit.connection_point_counter.lock().unwrap() += 1;
        ConnectionPoint {
            id: *circuit.connection_point_counter.lock().unwrap(),
            voltage: 0.0,
            circuit_id: circuit.id,
        }
    }

    const fn get_circuit_id(&self) -> u64 {
        self.circuit_id
    }
}

impl PartialEq for ConnectionPoint {
    fn eq(&self, other: &Self) -> bool {
        self.id == other.id
            && self.circuit_id
                == other.circuit_id
    }
}

impl Eq for ConnectionPoint {}

impl PartialOrd for ConnectionPoint {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        match self.circuit_id.cmp(&other.circuit_id) {
            Ordering::Equal => Some(self.id.cmp(&other.id)),
            Ordering::Greater => Some(Ordering::Greater),
            Ordering::Less => Some(Ordering::Less),
        }
    }
}

impl Ord for ConnectionPoint {
    fn cmp(&self, other: &Self) -> Ordering {
        self.partial_cmp(other).unwrap()
    }
}

//Designed to be thread safe, since simulation may want to be deferred to another thread
unsafe impl Send for Circuit<'_> {}
unsafe impl Sync for Circuit<'_> {}

unsafe impl Send for ConnectionPoint {}
unsafe impl Sync for ConnectionPoint {}


