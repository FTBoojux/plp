use std::fmt::{Debug, Formatter};
use crate::web_client::enums::ContentType;


#[derive(Clone)]
pub struct MultipartFile {
    pub filename: String,
    pub content_type: ContentType,
    pub bytes: Vec<u8>
}

impl Debug for MultipartFile {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("MultiPartFile")
            .field("filename",&self.filename)
            .field("content_type", &self.content_type)
            .finish()
    }
}

pub struct MultipartFileBuilder {
    pub filename: Option<String>,
    pub content_type: Option<String>,
    pub bytes: Option<Vec<u8>>
}

impl MultipartFile {
    pub fn builder() -> MultipartFileBuilder {
        MultipartFileBuilder{
            filename: None,
            content_type: None,
            bytes: None
        }
    }
}

impl MultipartFileBuilder {
    pub fn filename(mut self, filename : impl Into<String>) -> Self {
        self.filename = Some(filename.into());
        self
    }
    pub fn content_type(mut self, content_type: impl Into<String>) -> Self {
        self.content_type = Some(String::from(content_type.into()));
        self
    }
    pub fn bytes(mut self, bytes: Vec<u8>) -> Self {
        self.bytes = Some(bytes);
        self
    }
    pub fn build(self) -> MultipartFile {
        let filename = if self.filename.is_none() { String::new() } else { self.filename.unwrap().clone() };
        let content_type = if self.content_type.is_none() {
            ContentType::BINARY
        } else {
            ContentType::parse(self.content_type.unwrap().as_str())
        };
        let bytes = if self.bytes.is_none() { Vec::new() } else { self.bytes.unwrap() };
        MultipartFile{
             filename,
             content_type,
             bytes
         }
    }
}