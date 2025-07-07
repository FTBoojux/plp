use std::collections::HashMap;
use std::net::TcpListener;

pub struct WebClient {
    tcp_listener: TcpListener,
    handlers:  HashMap<String, Handler>,
}

type Handler = Box<dyn Fn() -> Result<String, std::io::Error>>;

pub struct WebClientBuilder {
    port: Option<u16>,
    url: Option<String>,
    handlers: HashMap<String, Handler>,
}

impl WebClientBuilder {
    pub fn new() -> WebClientBuilder{
        WebClientBuilder {
            port: None,
            url: None,
            handlers: HashMap::new(),
        }
    }
    pub fn port(mut self, port: u16) -> WebClientBuilder {
        self.port = Some(port);
        self
    }
    pub fn url(mut self, url: &str) -> WebClientBuilder {
        self.url = Some(url.to_string());
        self
    }
    pub fn build(self) -> Result<WebClient, std::io::Error> {
        let address = format!(
            "{}:{}",
            self.url.as_ref().unwrap_or( &"127.0.0.1".to_string()),
            self.port.unwrap_or(0)
        );
        match TcpListener::bind(address) {
            Ok(tcp_listener) => Ok(WebClient{ tcp_listener, handlers: self.handlers }),
            Err(e) => Err(e),
        }
    }
    pub fn route(mut self, path: &str, handler: Handler ) -> Result<WebClientBuilder, std::io::Error> {
        if self.handlers.contains_key(path) {
            return Err(std::io::Error::new(std::io::ErrorKind::AlreadyExists, format!("Handler already exists: {}", path)));
        }
        self.handlers.insert(path.to_string(), handler);
        Ok(self)
    }
}


#[cfg(test)]
mod test{
    use crate::web_client::web_client::WebClientBuilder;

    #[test]
    pub fn test_create_web_client_with_unused_port_successful(){
        let client = WebClientBuilder::new().port(8000).build();
    }
    #[test]
    pub fn test_create_web_client_with_used_port_should_fail(){
        let client = WebClientBuilder::new().port(8000).build();
        let result = WebClientBuilder::new().port(8000).build();
        let mut failed = false;
        match result {
            Ok(_)=>{failed=false;}
            Err(e)=>{failed = true;}
        }
        assert!(failed)
    }
}

#[cfg(test)]
mod link_test{
    use std::io::Error;
    use crate::web_client::health_check::health_check;
    use crate::web_client::web_client::*;
    #[test]
    pub fn add_handler_successfully(){
        let client = WebClientBuilder::new().port(8000).route("health_check",Box::new(health_check)).unwrap();
    }
    #[test]
    pub fn add_handler_with_duplicated_url_should_fail(){
        let client = WebClientBuilder::new().port(8000)
            .route("health_check",Box::new(health_check)).unwrap()
            .route("health_check",Box::new(health_check))
            ;
        let mut failed = false;
        match client {
            Ok(_) => {failed=false;}
            Err(e) => {failed=true;}
        }
        assert!(failed)
    }
}