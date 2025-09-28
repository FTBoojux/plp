use crate::web_client::enums::HttpHeadersEnum;
use crate::web_client::utils::json::bson::JsonType;
use crate::web_client::utils::web::form_data::FormData;
use std::collections::HashMap;
use std::num::ParseIntError;

pub enum RequestError{
    MissingBoundary,
    InvalidBody
}

#[derive(Debug)]
#[derive(Clone)]
pub struct HttpHeader {
    header: HashMap<String, String>
}
#[derive(Debug,Clone)]
pub struct HttpRequest {
    pub method: String,
    pub path: String,
    pub version: String,
    pub headers: HttpHeader,
    pub params: HashMap<String, String>,
    pub form_data: FormData,
    pub request_body: Option<JsonType>,
}
impl HttpHeader {
    pub fn new() -> Self {
        Self{
            header: HashMap::new()
        }
    }
    pub fn insert(&mut self, key: impl Into<String>, value: String) ->Option<String> {
        self.header.insert(key.into(), value)
    }
    pub fn get_content_length(&self) -> Option<i32> {
        let content_length = self.header.get(HttpHeadersEnum::ContentLength.headers());
        if let Some(content_length) = content_length {
            let parse_result = content_length.trim().parse::<i32>();
            match parse_result {
                Ok(content_length) => return Some(content_length),
                Err(err) => panic!("Failed to get Content-Length from Http Header! err: {}",err)
            }
        }
        None
    }
    pub fn get_content_type(&self) -> Option<String> {
        self.get_by_header(HttpHeadersEnum::ContentType)
    }
    pub fn get_by_header(&self, header: HttpHeadersEnum) -> Option<String> {
        let value = self.header.get(header.headers());
        match value {
            None => None,
            Some(value) => Some(value.clone())
        }
    }
    pub fn get(&self, header: &str) -> Option<&String> {
        self.header.get(header)
    }
    pub fn get_as_u32(&self, header: &str) -> Option<u32> {
        let value = self.header.get(header);
        match value {
            None => None,
            Some(value) => {
                let value = value.parse::<u32>();
                match value {
                    Ok(value) => {Some(value)}
                    Err(err) => {panic!("Failed to get {} as u32!",header)}
                }
            }
        }
    }
    pub fn cookie(&self) -> Cookie{
        let cookie = self.get("Cookie");
        if let Some(cookie) = cookie {
            Cookie::from_string(cookie)
        } else {
            Cookie::new()
        }
    }
}
pub struct HttpResponse {
    pub http_version: String,
    pub status_code: u32,
    pub response_phrase: String,
    pub body: String
}

impl HttpResponse {
    pub fn create( status_code : u32, response_phrase : String, body : String) -> Self {
        Self{
            http_version: String::from("HTTP/1.1"),
            status_code,
            response_phrase,
            body
        }
    }
}
#[derive(Clone, Debug)]
pub struct Cookie {
    cookie: HashMap<String, String>
}

impl Cookie {
    pub fn new()->Self{
       Self{
           cookie: HashMap::new()
       }
    }
    pub fn from_string(cookie:impl Into<String>) -> Self{
        let _cookie = cookie.into();
        let _cookies = _cookie.split(';');
        let mut result = Cookie::new();
        for cookie in _cookies {
            if let Some(index) = cookie.find('=') {
                let key = &cookie[0..index];
                let value = cookie[index+1..].trim();
                result.add(key,value);
            }
        }
        result
    }
    pub fn add(&mut self, key: impl Into<String>, value: impl Into<String>) {
        self.cookie.insert(key.into(),value.into());
    }
    pub fn get(&self, key: impl Into<String>) -> Option<String> {
        match self.cookie.get(key.into().as_str()) {
            None => {None}
            Some(value) => {Some(value.to_string())}
        }
    }
}

mod cookie_test {
    use crate::web_client::utils::web::request::Cookie;
    use crate::web_client::web_client::HttpHeader;

    #[test]
    pub fn add_cookie(){
        let mut cookie = Cookie::new();
        cookie.add("cookie1","hello");
    }
    #[test]
    pub fn get_cookie(){
        let mut cookie = Cookie::new();
        cookie.add("cookie1", "hello");
        assert_eq!(cookie.get("cookie1"),Some("hello".to_string()));
    }
    #[test]
    pub fn create_cookie_from_raw_string(){
        let header_cookie = "cookie1=hello;cookie2=Boojux";
        let cookie:Cookie = Cookie::from_string(header_cookie);
        assert_eq!(cookie.get("cookie1"),Some("hello".to_string()));
        assert_eq!(cookie.get("cookie2"),Some("Boojux".to_string()));
    }
    #[test]
    pub fn get_cookie_from_header(){
        let mut header = HttpHeader::new();
        header.insert("Cookie","cookie1=hello;cookie2=Boojux".to_string());
        let cookie = header.cookie();
        assert_eq!(cookie.get("cookie1"),Some("hello".to_string()));
        assert_eq!(cookie.get("cookie2"),Some("Boojux".to_string()));
    }
}