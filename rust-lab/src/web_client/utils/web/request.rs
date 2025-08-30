use std::collections::HashMap;
use crate::web_client::enums::{ContentType, HttpHeadersEnum};
use crate::web_client::utils::json::bson::JsonType;
use crate::web_client::utils::web::form_data::FormData;

pub enum RequestError{
    MissingBoundary,
    InvalidBody
}

#[derive(Debug)]
pub struct HttpHeader {
    header: HashMap<String, String>
}
#[derive(Debug)]
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
}