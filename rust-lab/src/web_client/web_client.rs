use std::net::TcpListener;

pub struct WebClient {
    tcp_listener: TcpListener
}

pub struct WebClientBuilder {
    port: Option<u16>,
    url: Option<String>,
}

impl WebClientBuilder {
    pub fn new() -> WebClientBuilder{
        WebClientBuilder {
            port: None,
            url: None,
        }
    }
    pub fn port(&mut self, port: u16) -> &mut WebClientBuilder {
        self.port = Some(port);
        self
    }
    pub fn url(&mut self, url: &str) -> &mut WebClientBuilder {
        self.url = Some(url.to_string());
        self
    }
    pub fn build(&self) -> Result<WebClient, std::io::Error> {
        let address = format!(
            "{}:{}",
            self.url.as_ref().unwrap_or( &"127.0.0.1".to_string()),
            self.port.unwrap_or(0)
        );
        match TcpListener::bind(address) {
            Ok(tcp_listener) => Ok(WebClient{ tcp_listener }),
            Err(e) => Err(e),
        }
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