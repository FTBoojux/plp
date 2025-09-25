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