use std::collections::HashMap;
use std::io;
use std::io::{BufRead, BufReader, ErrorKind, Read, Write};
use std::net::{SocketAddr, TcpListener, TcpStream};
use std::num::ParseIntError;
use std::sync::Arc;
use log::log;
use socket2::{Domain, Socket, Type};
use crate::web_client::enums::{PathType, RequestHandler};
use crate::web_client::error::err::ParseError;
use crate::web_client::utils::fog::fog;
use crate::web_client::utils::fog::fog::log;
use crate::web_client::utils::json::bson::{JsonParser, JsonType};
use crate::web_client::utils::thread_pool::ThreadPool;
use crate::web_client::utils::web::route_pattern::RoutePattern;
use crate::web_client::utils::web::route_tree::RouteTree;

pub struct WebClient {
    tcp_listener: TcpListener,
    handlers:  HashMap<String, HashMap<String,RequestHandler>>,
    route_tree: RouteTree,
    thread_pool: ThreadPool
}
#[derive(Debug)]
pub struct HttpRequest {
    method: String,
    path: String,
    version: String,
    pub headers: HashMap<String, String>,
    pub params: HashMap<String, String>,
    pub request_body: Option<JsonType>,
}
pub struct RequestMatchResult<'a>{
    pub matched: bool,
    pub request: HttpRequest,
    pub request_handler: &'a RequestHandler,
    pub path_variables: HashMap<String, String>,
}
struct HttpResponse {
    http_version: String,
    // headers: HashMap<String, String>,
    status_code: u32,
    response_phrase: String,
    body: String,
}

impl RequestMatchResult<'_>{
    pub fn handle(self)->Result<String, std::io::Error>{
        (self.request_handler)(self)
    }
}
impl WebClient {
    pub fn listen(self: Arc<Self>) -> (){
        println!("Listening for connections on {}", self.tcp_listener.local_addr().unwrap());
            for input in self.tcp_listener.incoming() {
                match input {
                    Ok(_socket) => {
                        let client = self.clone();
                        self.thread_pool.execute(move||{
                            client.handle_connection(_socket);
                        })
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
               // println!("{} {} {}",request.method,request.path,request.version );
               log(format!("{} {} {}",request.method, request.path, request.version));
               log(format!("{:?}", request.request_body));
               let response = self.route_request(request);
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
        let origin_path = request_line_vec[1].to_string();
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
        let mut params: HashMap<String, String> = HashMap::new();
        let paths:Vec<&str> = origin_path.split("?").collect();
        let path = paths[0].to_string();
        if paths.len() > 1{
            let pairs:Vec<&str> = paths[1].split("&").collect();
            for pair in pairs {
                let kv:Vec<&str> = pair.split("=").collect();
                params.insert(kv[0].to_string(), if kv.len() > 1 { kv[1].to_string() } else { "".to_string() } );
            }
        }

        let mut headers:HashMap<String, String> = HashMap::new();
        lines.iter().for_each(|line| {
            let header:Vec<&str> = line.split(": ").collect();
            let key = header[0];
            let value = if header.len() > 1 {header[1]} else {""};
            headers.insert(key.to_string(), value.to_string());
        });
        let mut request_body = None;
        if let Some(content_length) = headers.get("Content-Length") {
            let parse = content_length.trim().parse::<usize>();
            let json_type = match parse {
                Ok(content_length) => {
                    let mut v8: Vec<u8> = Vec::new();
                    let mut u8:[u8;1024] = [0;1024];
                    let mut read_length = 0;
                    loop {
                        if read_length >= content_length {
                            break;
                        }
                        let length = buf_reader.read(&mut u8).expect("Faild to read the request body!");
                        read_length += length;
                        for ch in u8 {
                            v8.push(ch);
                        }
                    }
                    let body_string = String::from_utf8(v8).expect("Failed to convert the request body!");
                    let mut parser = JsonParser::new(body_string.as_str());
                    let json_type = match parser.parse() {
                        Ok(res) => {
                            Some(res)
                        }
                        Err(ParseError::InvalidParseBodyError(s)) => {
                            log(s);
                            None
                        },
                        Err(ParseError::InvalidRequestBodyError(s)) => {
                            log(s);
                            None
                        }
                    };
                    json_type
                }
                Err(e) => {
                    log(e.to_string());
                    None
                }
            };
            request_body = json_type;
        }
        Ok(
            HttpRequest{
                method,
                path,
                params,
                version,
                headers,
                request_body
            }
        )
    }

    fn route_request(&self, request: HttpRequest) -> HttpResponse {

        if let Ok(match_result) = self.find_request_handler(request){
            match match_result.handle(){
                Ok(response)=>{
                    HttpResponse{
                        http_version: String::from("HTTP/1.1"),
                        status_code: 200,
                        response_phrase: String::from("OK"),
                        body: response,
                    }
                },
                Err(e)=>{
                    // println!("Error: {}", e);
                    log(format!("Error: {}", e));
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

    fn find_request_handler(&self, http_request: HttpRequest) -> Result<RequestMatchResult, std::io::ErrorKind> {
        let static_result = self.handlers.get(http_request.path.as_str());
        if static_result.is_some() {
            if let Some(static_result) = static_result.unwrap().get(http_request.method.as_str()){
                return Ok(RequestMatchResult{
                    matched: true,
                    request: http_request,
                    request_handler: static_result,
                    path_variables: HashMap::new(),
                });
            }
        }
        // if !static_result.is_none() {
        //     Ok(RequestMatchResult{
        //         matched: true,
        //         request: http_request,
        //         request_handler: static_result.unwrap(),
        //         path_variables: HashMap::new(),
        //     })
        // }
        if let Some(route) = self.route_tree.find(http_request.method.as_str(), http_request.path.as_str()) {
            Ok(
                RequestMatchResult {
                    matched: true,
                    request: http_request,
                    request_handler: route.request_handler,
                    path_variables: route.path_variables.clone(),
                }
            )
        } else {
            Err(ErrorKind::NotFound)
        }
    }
}


pub struct WebClientBuilder {
    port: Option<u16>,
    url: Option<String>,
    backlog: i32,
    threads: u32,
    handlers: HashMap<String, HashMap<String, RequestHandler>>,
    route_tree: RouteTree,
}

impl WebClientBuilder {
    pub fn new() -> WebClientBuilder{
        WebClientBuilder {
            port: None,
            url: None,
            backlog: 200,
            threads: 4,
            handlers: HashMap::new(),
            route_tree: RouteTree::new("","","")
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
    pub fn backlog(mut self, backlog: i32) -> WebClientBuilder {
        self.backlog = backlog;
        self
    }
    pub fn threads(mut self, threads: u32) -> WebClientBuilder {
        self.threads = threads;
        self
    }
    pub fn build(self) -> Result<Arc<WebClient>, std::io::Error> {
        fog::init();
        let socket = Socket::new(Domain::IPV4, Type::STREAM, None)?;
        let address: SocketAddr = format!(
                "{}:{}",
                self.url.as_ref().unwrap_or( &"127.0.0.1".to_string()),
                self.port.unwrap_or(0)
            ).parse().unwrap();
        let address = address.into();
        socket.bind(&address)?;
        socket.listen(self.backlog)?;
        socket.set_tcp_nodelay(true)?;
        Ok(Arc::new(WebClient{
            tcp_listener:socket.into(),
            handlers: self.handlers,
            route_tree: self.route_tree,
            thread_pool: ThreadPool::new(self.threads),
        }))
    }
    pub fn route(mut self, http_type: &str, path: &str, handler: RequestHandler) -> Result<WebClientBuilder, std::io::Error> {
        if self.handlers.contains_key(path) {
            return Err(std::io::Error::new(std::io::ErrorKind::AlreadyExists, format!("Handler already exists: {}", path)));
        }
        let pattern = RoutePattern::parse(http_type, path, handler);
        match pattern.path_type {
            PathType::STATIC => {
                // self.handlers.insert(path.to_string(), pattern.handler.second.unwrap());
                if !self.handlers.contains_key(path) {
                    self.handlers.insert(path.to_string(),HashMap::new());
                }
                self.handlers.get_mut(path).unwrap().insert(http_type.to_string(), pattern.handler.second.unwrap());
            }
            PathType::DYNAMIC => {
                self.route_tree.add_route(pattern)
            }
        }
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
        let client = WebClientBuilder::new().port(8000).route("GET","health_check",Box::new(health_check)).unwrap();
    }
    #[test]
    pub fn add_handler_with_duplicated_url_should_fail(){
        let client = WebClientBuilder::new().port(8000)
            .route("GET","health_check",Box::new(health_check)).unwrap()
            .route("GET","health_check",Box::new(health_check))
            ;
        let mut failed = false;
        match client {
            Ok(_) => {failed=false;}
            Err(e) => {failed=true;}
        }
        assert!(failed)
    }
}