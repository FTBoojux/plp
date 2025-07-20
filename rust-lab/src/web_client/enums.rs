use crate::web_client::web_client::{HttpRequest, RequestMatchResult};

pub type RequestHandler = Box<dyn Fn(&RequestMatchResult) -> Result<String, std::io::Error>>;
#[derive(Debug)]
pub enum PathType{
    // static path, like : /user/info
    STATIC,
    // dynamic path, like: /users/{page}/{size}
    DYNAMIC,
}

pub enum Wildcard{
    Wildcard,
}