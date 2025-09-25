use crate::web_client::utils::web::request::HttpRequest;
use crate::web_client::web_client::{HttpResponse, RequestMatchResult};

pub type RequestHandler = Box<dyn Fn(RequestMatchResult) -> Result<String, std::io::Error> + Send + Sync + 'static>;

pub type Middleware = Box<dyn Fn(&mut HttpRequest,&mut HttpResponse)->bool+ Send + Sync + 'static>;

#[derive(Debug)]
pub enum PathType{
    // static path, like : /user/info
    STATIC,
    // dynamic path, like: /users/{page}/{size}
    DYNAMIC,
}

pub static WILDCARD_STR: &'static str = "*";
pub const SEMICOLON: &str = "; ";
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

pub enum HttpHeadersEnum {
    ContentType,
    ContentLength,
}

impl HttpHeadersEnum {
    pub fn headers(&self) -> &'static str {
        match self {
            HttpHeadersEnum::ContentType => "Content-Type",
            HttpHeadersEnum::ContentLength => "Content-Length",
        }
    }
}

#[derive(Debug, Clone)]
pub enum ContentType {
    JSON,
    FormData,
    FormUrlencoded,
    ImagePng,
    ImageJpeg,
    ImageGif,
    ImageWebp,
    ImageSvg,
    PDF,
    MSWORD,
    DOCX,
    PLAIN,
    HTML,
    CSV,
    CSS,
    JAVASCRIPT,
    XML,
    ZIP,
    BINARY,
    MPEG,
    WAV,
    AudioOgg,
    MP3,
    MP4,
    WEBM,
    VideoOgg,
}

impl ContentType {
    pub fn values(&self) ->&'static str {
        match self {
            ContentType::JSON => "application/json",
            ContentType::FormData => "multipart/form-data",
            ContentType::FormUrlencoded => "application/x-www-form-urlencoded",
            ContentType::ImagePng => "image/png",
            ContentType::ImageJpeg => "image/jpeg",
            ContentType::ImageGif => "image/gif",
            ContentType::ImageWebp => "image/webp",
            ContentType::ImageSvg => "image/svg+xml",
            ContentType::PDF => "application/pdf",
            ContentType::MSWORD => "application/msword",
            ContentType::DOCX => "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            ContentType::PLAIN => "text/plain",
            ContentType::HTML => "text/html",
            ContentType::CSV => "text/csv",
            ContentType::CSS => "text/css",
            ContentType::JAVASCRIPT => "text/javascript",
            ContentType::XML => "application/xml",
            ContentType::ZIP => "application/zip",
            ContentType::BINARY => "application/octet-stream",
            ContentType::MPEG => "audio/mpeg",
            ContentType::WAV =>"audio/wav",
            ContentType::AudioOgg => "audio/ogg",
            ContentType::MP3 => "audio/mp3",
            ContentType::MP4 => "video/mp4",
            ContentType::WEBM => "video/webm",
            ContentType::VideoOgg => "video/ogg",
        }
    }
    pub fn parse(content_type: &str) -> ContentType {
        match content_type {
            "application/json" => ContentType::JSON,
            "multipart/form-data" => ContentType::FormData,
            "application/x-www-form-urlencoded" => ContentType::FormUrlencoded,
            "image/png" => ContentType::ImagePng,
            "image/jpeg" => ContentType::ImageJpeg,
            "image/gif" => ContentType::ImageGif,
            "image/webp" => ContentType::ImageWebp,
            "image/svg+xml" => ContentType::ImageSvg,
            "application/pdf" => ContentType::PDF,
            "application/msword" => ContentType::MSWORD,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" => ContentType::DOCX,
            "text/plain" => ContentType::PLAIN,
            "text/html" => ContentType::HTML,
            "text/csv" => ContentType::CSV,
            "text/css" => ContentType::CSS,
            "text/javascript" => ContentType::JAVASCRIPT,
            "application/xml" => ContentType::XML,
            "application/zip" => ContentType::ZIP,
            "application/octet-stream" => ContentType::BINARY,
            "audio/mpeg" => ContentType::MPEG,
            "audio/wav" => ContentType::WAV,
            "audio/ogg" => ContentType::AudioOgg,
            "audio/mp3" => ContentType::MP3,
            "video/mp4" => ContentType::MP4,
            "video/webm" => ContentType::WEBM,
            "video/ogg" => ContentType::VideoOgg,
            _ => ContentType::BINARY
        }
    }
}