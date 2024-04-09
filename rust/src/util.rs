use std::slice::Iter;

pub struct ArrayBuilder<T> {
    arrays: Vec<Box<[T]>>,
}

impl<T> ArrayBuilder<T> {

    pub fn new() -> ArrayBuilder<T> {
        ArrayBuilder {
            arrays: Vec::new(),
        }
    }

    pub fn with_capacity(size: usize) -> ArrayBuilder<T> {
        ArrayBuilder {
            arrays: Vec::with_capacity(size),
        }
    }

    pub fn push_array(&mut self, arr: Box<[T]>) {
        self.arrays.push(arr);
    }

    pub fn to_array(self) -> Box<[T]> {
        let size: usize = self.arrays.iter().map(|a| a.len()).sum();
        let mut ret = Box::new([]);



        ret
    }

    pub fn iter(&self) -> ArrayBuilderIter<T> {
        self.into_iter()
    }
}

impl<T: Clone> IntoIterator for ArrayBuilder<T> {
    type Item = T;
    type IntoIter = ArrayBuilderIntoIter<T>;

    fn into_iter(self) -> Self::IntoIter {
        ArrayBuilderIntoIter {
            array_builder: self,
            array_num: 0,
            array_index: 0,
        }
    }
}

impl<'a, T> IntoIterator for &'a ArrayBuilder<T> {
    type Item = &'a T;
    type IntoIter = ArrayBuilderIter<'a, T>;

    fn into_iter(self) -> Self::IntoIter {
        ArrayBuilderIter {
            array_builder: self,
            array_num: 0,
            array_index: 0,
        }
    }

}

pub struct ArrayBuilderIter<'a, T> {
    array_builder: &'a ArrayBuilder<T>,
    array_num: usize,
    array_index: usize,
}

pub struct ArrayBuilderIntoIter<T> {
    array_builder: ArrayBuilder<T>,
    array_num: usize,
    array_index: usize,
}

impl<'a, T> Iterator for ArrayBuilderIter<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<Self::Item> {
        loop {
            match self.array_builder.arrays.get(self.array_num) {
                None => return None,
                Some(arr) => {
                    if self.array_index < arr.len() {
                        self.array_index += 1;
                        return Some(&self.array_builder.arrays[self.array_num][self.array_index]);
                    }
                    self.array_num += 1;
                    self.array_index = 0;
                }
            }
        }
    }
}

impl<T: Clone> Iterator for ArrayBuilderIntoIter<T> {
    type Item = T;

    fn next(&mut self) -> Option<Self::Item> {
        loop {
            match self.array_builder.arrays.get(self.array_num) {
                None => return None,
                Some(arr) => {
                    if self.array_index < arr.len() {
                        self.array_index += 1;
                        return Some(self.array_builder.arrays[self.array_num][self.array_index].clone());
                    }
                    self.array_num += 1;
                    self.array_index = 0;
                }
            }
        }
    }
}