use crate::web_client::enums::{ContentType, HttpMethod, Middleware, PathType, RequestHandler};
use crate::web_client::error::err::ParseError;
use crate::web_client::utils::fog::fog;
use crate::web_client::utils::fog::fog::log;
use crate::web_client::utils::thread_pool::ThreadPool;
use crate::web_client::utils::web::body_parser::get_parser_by_content_type;
use crate::web_client::utils::web::form_data::FormData;
pub(crate) use crate::web_client::utils::web::request::{HttpHeader, HttpRequest, HttpResponse};
use crate::web_client::utils::web::route_pattern::RoutePattern;
use crate::web_client::utils::web::route_tree::RouteTree;
use socket2::{Domain, Socket, Type};
use std::collections::HashMap;
use std::io;
use std::io::{BufRead, BufReader, Error, ErrorKind, Write};
use std::net::{SocketAddr, TcpListener, TcpStream};
use std::sync::Arc;

pub struct WebClient {
    tcp_listener: TcpListener,
    handlers:  HashMap<String, HashMap<String,RequestHandler>>,
    route_tree: RouteTree,
    thread_pool: ThreadPool,
    pre_handler: Vec<Middleware>,
    post_handler: Vec<Middleware>
}

pub struct RequestMatchResult<'a>{
    pub matched: bool,
    pub request: HttpRequest,
    pub request_handler: &'a RequestHandler,
    pub path_variables: HashMap<String, String>,
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
           Ok(mut request) =>{
               log(format!("{} {} {}",request.method, request.path, request.version));
               log(format!("{:?}", request.request_body));
               let mut response = HttpResponse::create(
                   500,
                   "Sever Error".to_string(),
                    "".to_string(),
               );
               if !Self::pass_middleware(&self.pre_handler,&mut request, &mut response) {
                   tcp_stream.write(response.build().as_bytes()).expect("TODO: panic message");
                   return;
               }
               let mut clone_request = request.clone();
               response = self.route_request(request);
               Self::pass_middleware(&self.post_handler, &mut clone_request, &mut response);
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

        let mut headers= HttpHeader::new();
        lines.iter().for_each(|line| {
            let header:Vec<&str> = line.split(": ").collect();
            let key = header[0];
            let value = if header.len() > 1 {header[1]} else {""};
            headers.insert(key, value.to_string());
        });
        let content_type = headers.get_content_type();
        if let Some(content_type) =  content_type {
            let position_of_semicolon = content_type.find(';');
            let position_of_semicolon = match position_of_semicolon {
                None => content_type.len(),
                Some(index) => index
            };
            let (content_type, _boundary) = content_type.split_at(position_of_semicolon);
            let handler = get_parser_by_content_type(ContentType::parse(content_type.trim()));
            let parse_result = (*handler)(&mut buf_reader, &headers);
            match parse_result {
                Ok((form_data, request_body)) => Ok(HttpRequest{
                    method,
                    path,
                    params,
                    version,
                    headers,
                    form_data,
                    request_body:Some(request_body)
                }),
                Err(err) => {
                    match err {
                        ParseError::InvalidRequestError(err) => {
                            log(err);
                        }
                        ParseError::InvalidParseBodyError(err) => {
                            log(err);
                        }
                    }
                    Ok(HttpRequest{
                        method,
                        path,
                        params,
                        version,
                        headers,
                        form_data: FormData::new(),
                        request_body: None
                    })
                }
            }
        } else {
            Ok(
                HttpRequest{
                    method,
                    path,
                    params,
                    version,
                    headers,
                    form_data: FormData::new(),
                    request_body: None
                }
            )
        }
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
                    log(format!("Error: {}", e));
                    HttpResponse::create(
                        500,
                        String::from("Internal Server Error"),
                        e.to_string(),
                    )
                }
            }
        }else{
            HttpResponse::create(
                404,
                String::from("Not Found"),
                String::new(),
            )
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

    fn pass_middleware(handlers: &Vec<Middleware>, http_request: &mut HttpRequest, http_response: &mut HttpResponse) -> bool {
        for pre_handler in handlers {
            if !pre_handler(http_request,http_response) {
                return false;
            }
        }
        true
    }
}


pub struct WebClientBuilder {
    port: Option<u16>,
    url: Option<String>,
    backlog: i32,
    threads: u32,
    handlers: HashMap<String, HashMap<String, RequestHandler>>,
    route_tree: RouteTree,
    pre_handler: Vec<Middleware>,
    post_handler: Vec<Middleware>
}

impl WebClientBuilder {
    pub fn new() -> WebClientBuilder{
        WebClientBuilder {
            port: None,
            url: None,
            backlog: 200,
            threads: 4,
            handlers: HashMap::new(),
            route_tree: RouteTree::new("","",""),
            pre_handler: Vec::new(),
            post_handler: Vec::new()
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
            pre_handler: self.pre_handler,
            post_handler: self.post_handler
        }))
    }
    pub fn add_pre_handler(mut self, middleware: Middleware) -> WebClientBuilder {
        self.pre_handler.push(middleware);
        self
    }
    pub fn add_post_handler(mut self, middleware: Middleware) -> WebClientBuilder {
        self.post_handler.push(middleware);
        self
    }
    pub fn route0(mut self, http_type: &str, path: &str, handler: RequestHandler) -> Result<WebClientBuilder, std::io::Error> {
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
    pub fn route(self, http_type: HttpMethod, path: &str, handler: RequestHandler) -> Result<WebClientBuilder, Error> {
        self.route0(http_type.value(), path, handler)
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
        body
    }
}

#[cfg(test)]
mod test{
    use crate::web_client::web_client::WebClientBuilder;

    #[test]
    pub fn test_create_web_client_with_unused_port_successful(){
        let _client = WebClientBuilder::new().port(8000).build();
    }
    #[test]
    pub fn test_create_web_client_with_used_port_should_fail(){
        let _client = WebClientBuilder::new().port(8000).build();
        let result = WebClientBuilder::new().port(8000).build();
        let failed;
        match result {
            Ok(_)=>{failed=false;}
            Err(_e)=>{failed = true;}
        }
        assert!(failed)
    }
}

#[cfg(test)]
mod link_test{
    use crate::web_client::health_check::health_check;
    use crate::web_client::web_client::*;
    #[test]
    pub fn add_handler_successfully(){
        let _client = WebClientBuilder::new().port(8000).route0("GET", "health_check", Box::new(health_check)).unwrap();
    }
    #[test]
    pub fn add_handler_with_duplicated_url_should_fail(){
        let client = WebClientBuilder::new().port(8000)
            .route0("GET", "health_check", Box::new(health_check)).unwrap()
            .route0("GET", "health_check", Box::new(health_check))
            ;
        let failed;
        match client {
            Ok(_) => {failed=false;}
            Err(_e) => {failed=true;}
        }
        assert!(failed)
    }
}