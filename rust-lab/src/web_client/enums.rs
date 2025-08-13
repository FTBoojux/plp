use crate::web_client::web_client::{HttpRequest, RequestMatchResult};

pub type RequestHandler = Box<dyn Fn(RequestMatchResult) -> Result<String, std::io::Error> + Send + Sync + 'static>;
#[derive(Debug)]
pub enum PathType{
    // static path, like : /user/info
    STATIC,
    // dynamic path, like: /users/{page}/{size}
    DYNAMIC,
}

pub static WILDCARD_STR: &'static str = "*";

pub enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTION,
    TRACE,
    CONNECT,
}

impl HttpMethod {
    pub fn value(self) -> &'static str{
        match self {
            HttpMethod::GET => "GET",
            HttpMethod::POST => "POST",
            HttpMethod::PUT => "PUT",
            HttpMethod::PATCH => "PATCH",
            HttpMethod::DELETE => "DELETE",
            HttpMethod::HEAD => "HEAD",
            HttpMethod::OPTION => "OPTION",
            HttpMethod::TRACE => "TRACE",
            HttpMethod::CONNECT => "CONNECT",
        }
    }
}