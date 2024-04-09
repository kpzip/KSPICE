use std::cmp::Ordering;
use super::components::component::Component;
use std::collections::BTreeSet;
use std::sync::{Arc, MutexGuard};
use std::sync::Mutex;
use crate::util::ArrayBuilder;

static CIRCUIT_INDEX_COUNTER: Mutex<u64> = Mutex::new(0);

pub struct Circuit {
    connection_points: Mutex<BTreeSet<Arc<ConnectionPoint>>>,
    components: Mutex<Vec<Box<dyn Component>>>,
    has_ground: bool,
    connection_point_counter: Mutex<u64>,
    id: u64,
}

impl Circuit {
    pub fn new() -> Circuit {
        let mut counter: MutexGuard<u64> = CIRCUIT_INDEX_COUNTER.lock().unwrap();

        let ground: ConnectionPoint = ConnectionPoint {
            id: 0,
            voltage: 0.0,
            circuit_id: *counter,
        };

        let circuit: Circuit = Circuit {
            connection_point_counter: Mutex::new(0),
            components: Mutex::new(Vec::new()),
            connection_points: Mutex::new(BTreeSet::new()),
            has_ground: true,
            id: *counter,
        };

        *counter += 1;

        circuit.connection_points.lock().unwrap().insert(Arc::new(ground));

        circuit
    }

    pub fn new_connection_point(&mut self) -> Arc<ConnectionPoint> {
        let mut connection_points = self.connection_points.lock().unwrap();
        let point: Arc<ConnectionPoint> = Arc::new(ConnectionPoint::new(self));
        connection_points
            .insert(point.clone());
        point
    }

    pub fn get_ground(&self) -> Arc<ConnectionPoint> {
        //TODO maybe make this better in the future?
        assert!(self.has_ground);
        self.connection_points.lock().unwrap().first().unwrap().clone()
    }

    pub fn add_component(& mut self, component: impl Component + 'static)
    {
        self.components.lock().unwrap().push(Box::new(component));
    }

    pub fn simulation_step(&mut self, dt: f64) {

        let connection_points = self.connection_points.lock().unwrap();
        let components = self.components.lock().unwrap();

        let num_voltages = connection_points.len();
        let num_currents: usize = components.iter().map(|c| c.connection_count()).sum();
        let num_to_solve_for: usize = num_voltages + num_currents;

        let mut matrix: Vec<Vec<f64>> = Vec::with_capacity(num_to_solve_for);

        let mut constants: Vec<f64> = Vec::with_capacity(num_to_solve_for);


        let mut component_connection_index: usize = 0;

        //Enter in equations for each component
        for component in &*components {

            let connections: Box<[(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &dyn Component)]> = component.connections();
            let constraints: Box<[f64]> = component.constraints();
            let mut current_connection: usize = 0;

            for i in 0..connections.len() {

                let mut coefficients: Vec<f64> = Vec::with_capacity(num_to_solve_for);

                let current_dependence_index: usize = (2 * component.connection_count() + 1) * i;
                let voltage_dependence_index: usize = current_dependence_index + component.connection_count();
                let constant_dependence: f64 = constraints[2 * component.connection_count() + 1 * (2 * component.connection_count() + 1)];

                for j in 0..connections.len() {
                    let connection = &connections[j];

                    coefficients[(component_connection_index - current_connection) + j] = constraints[current_dependence_index + j];

                    coefficients[num_currents + self.connection_point_position(first_connection_point(connection))] += constraints[voltage_dependence_index + j];
                    coefficients[num_currents + self.connection_point_position(second_connection_point(connection))] += -constraints[voltage_dependence_index + j]
                }

                constants[component_connection_index] = constant_dependence;
                matrix[component_connection_index] = coefficients;

                component_connection_index += 1;
                current_connection += 1;
            }
        }

        // values for kirchhoff's junction laws
        let mut node_index: usize = num_currents;
        for connection_point in &*self.connection_points.lock().unwrap() {

            let mut coefficients: Vec<f64> = Vec::with_capacity(num_to_solve_for);

            if connection_point.eq(&self.get_ground()) {
                //Ground node should always be at 0
                coefficients[num_currents] = 1.0;
            }
            else {
                let mut arr: ArrayBuilder<(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &dyn Component)> = ArrayBuilder::new();
                components.iter().for_each(|c|  arr.push_array(c.connections()));
                let allConnections

            }

        }

    }

    #[inline]
    fn connection_point_position(&self, point: &Arc<ConnectionPoint>) -> usize {
        self.connection_points.lock().unwrap().iter().position(|p| (**p).eq(&**point)).unwrap()
    }
}

#[inline]
fn first_connection_point<'a>(pair: &'a(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'a dyn Component)) -> &'a Arc<ConnectionPoint> {
    let (first, second, _) = pair;
    match first.cmp(second) {
        Ordering::Less => first,
        Ordering::Equal => unreachable!(), //Might not actually be unreachable if components create garbage conenctions
        Ordering::Greater => second,
    }
}


#[inline]
fn second_connection_point<'a>(pair: &'a(Arc<ConnectionPoint>, Arc<ConnectionPoint>, &'a dyn Component)) -> &'a Arc<ConnectionPoint> {
    let (first, second, _) = pair;
    match first.cmp(second) {
        Ordering::Less => second,
        Ordering::Equal => unreachable!(), //Might not actually be unreachable if components create garbage conenctions
        Ordering::Greater => first,
    }
}



pub struct ConnectionPoint {
    id: u64,
    circuit_id: u64,
    voltage: f64,
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

    #[inline]
    fn set_voltage(&mut self, voltage: f64) {
        self.voltage = voltage;
    }

    #[inline]
    pub fn get_voltage(&self) -> f64 {
        self.voltage
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
unsafe impl Send for Circuit {}
unsafe impl Sync for Circuit {}

unsafe impl Send for ConnectionPoint {}
unsafe impl Sync for ConnectionPoint {}


