#[derive(Debug)]
pub struct Pair<T,U>{
    pub first: T,
    pub second: U,
}

impl <T,U> Pair<T,U>{
    pub fn new(first: T, second: U) -> Pair<T,U> {
        Pair{first, second }
    }
}