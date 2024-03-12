use std::alloc::{alloc, dealloc, Layout};
use std::mem;
use std::ops::{Deref, DerefMut, Index, IndexMut};

pub struct HeapArray<T> {
    pointer: *mut T,
    size: usize,
}

impl<T> HeapArray<T> {
    pub fn new(size: usize) -> HeapArray<T> {
        if size == 0 { todo!() }
        unsafe {
            let layout: Layout = Layout::from_size_align_unchecked(mem::size_of::<T>() * size, mem::align_of::<T>());
            let pointer: *mut T = alloc(layout);
            HeapArray {
                pointer,
                size,
            }
        }
    }

    pub unsafe fn index_unchecked(&self, index: usize) -> &T {
        &*((self.pointer as usize + index) as *mut T)
    }

    pub unsafe fn index_mut_unchecked(&mut self, index: usize) -> &mut T {
        &mut *((self.pointer as usize + index) as *mut T)
    }
}

impl<T> Drop for HeapArray<T> {
    fn drop(&mut self) {
        unsafe {
            let layout: Layout = Layout::from_size_align_unchecked(mem::size_of::<T>() * self.size, mem::align_of::<T>());
            dealloc(self.pointer, layout);
        }
    }
}

impl<T> Deref for HeapArray<T> {
    type Target = T;

    fn deref(&self) -> &Self::Target {
        unsafe { &*self.pointer }
    }
}

impl<T> DerefMut for HeapArray<T> {
    fn deref_mut(&mut self) -> &mut Self::Target {
        unsafe { &mut*self.pointer }
    }
}

/*
impl<'a, T> Index<usize> for HeapArray<T> {
    type Output = Option<&'a T>;

    fn index(&self, index: usize) -> Self::Output {
        if index >= self.size {
            None
        }
        unsafe {
            Some(&*((self.pointer as usize + index) as *const T))
        }
    }
}

impl<T> IndexMut<usize> for HeapArray<T> {

    fn index_mut(&mut self, index: usize) -> Self::Output {
        if index >= self.size {
            None
        }
        unsafe {
            Some(&mut *((self.pointer as usize + index) as *mut T))
        }
    }
}
*/