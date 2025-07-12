use std::collections::HashMap;
use std::io;
use std::io::{BufRead, BufReader, Read, Write};
use std::net::{TcpListener, TcpStream};

type Handler = Box<dyn Fn() -> Result<String, std::io::Error>>;
pub struct WebClient {
    tcp_listener: TcpListener,
    handlers:  HashMap<String, Handler>,
}
#[derive(Debug)]
struct HttpRequest {
    method: String,
    path: String,
    version: String,
    headers: HashMap<String, String>,
}
struct HttpResponse {
    http_version: String,
    // headers: HashMap<String, String>,
    status_code: u32,
    response_phrase: String,
    body: String,
}
impl WebClient {
    pub fn listen(&self) -> (){
        println!("Listening for connections on {}", self.tcp_listener.local_addr().unwrap());
        loop {
            let input = self.tcp_listener.accept();
            match input {
                Ok((mut _socket,addr)) => {
                    println!("New connection from: {}", addr);
                    self.handle_connection(_socket);
                }
                Err(e) => {
                    println!("Error: {}", e);
                    break;
                }
            }
        }
    }

    fn handle_connection(&self, mut tcp_stream: TcpStream) {
       match self.parse_request(&mut tcp_stream) {
           Ok(request) =>{
               let response = self.route_request(&request);
               println!("{} {} {}",request.method,request.path,request.version);
               tcp_stream.write(response.build().as_bytes()).expect("TODO: panic message");
           },
           Err(e) => {
               println!("Error: {}", e);
               tcp_stream.write(b"HTTP/1.1 400 Bad Request \r\n\r\n").ok();
           }
       }
    }

    fn parse_request(&self, stream: &mut TcpStream) -> Result<HttpRequest, io::Error> {
        let mut buf_reader = BufReader::new(stream);
        let mut line = String::new();
        let mut lines: Vec<String> = Vec::new();
        buf_reader.read_line(&mut line)?;
        let request_line_vec: Vec<&str> = line.trim().split_whitespace().collect();
        let method = request_line_vec[0].to_string();
        let path = request_line_vec[1].to_string();
        let version = request_line_vec[2].to_string();
        loop {
            line.clear();
            let size = buf_reader.read_line(&mut line)?;
            if size == 0 {
                break;
            }
            if line == "\r\n" || line == "\n" {
                break;
            }
            lines.push(line.clone());
        }
        // let request_line = &lines[0];
        let mut headers:HashMap<String, String> = HashMap::new();
        lines.iter().for_each(|line| {
            let header:Vec<&str> = line.split(": ").collect();
            let key = header[0];
            let value = if header.len() > 1 {header[1]} else {""};
            headers.insert(key.to_string(), value.to_string());
        });
        Ok(
            HttpRequest{
                method,
                path,
                version,
                headers,
            }
        )
    }

    fn route_request(&self, request: &HttpRequest) -> HttpResponse {

        if let Some(handler) = self.handlers.get(&request.path){
            match handler() {
                Ok(response) => {
                    HttpResponse{
                        http_version: String::from("HTTP/1.1"),
                        status_code: 200,
                        response_phrase: String::from("OK"),
                        body: response,
                    }
                },
                Err(e) => {
                    println!("Error: {}", e);
                    HttpResponse{
                        http_version: String::from("HTTP/1.1"),
                        status_code: 500,
                        response_phrase: String::from("Internal Server Error"),
                        body: e.to_string(),
                    }
                }
            }
        }else{
            HttpResponse{
                http_version: String::from("HTTP/1.1"),
                status_code: 404,
                response_phrase: String::from("Not Found"),
                body: String::new(),
            }
        }
    }
}


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

impl HttpResponse{
    pub fn build(&self) -> String{
        let mut body = String::new();
        body.push_str(self.http_version.as_str());
        body.push_str(" ");
        body.push_str(self.status_code.to_string().as_str());
        body.push_str(" ");
        body.push_str(self.response_phrase.as_str());
        body.push_str("\r\n");
        body.push_str("\r\n");
        body.push_str(self.body.as_str());
        body.push_str("\r\n");
        body
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