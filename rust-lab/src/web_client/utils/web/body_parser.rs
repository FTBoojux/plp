use crate::web_client::enums::{ContentType, HttpHeadersEnum, SEMICOLON};
use crate::web_client::error::err::ParseError;
use crate::web_client::utils::fog::fog::log;
use crate::web_client::utils::json::bson::{JsonParser, JsonType};
use crate::web_client::utils::web::form_data::{FormData, FormDataType};
use crate::web_client::utils::web::multipart::MultipartFile;
use crate::web_client::utils::web::request::HttpHeader;
use std::io::{BufReader, Read};
use std::net::TcpStream;
use std::string::ToString;


type BodyParser = Box<dyn Fn(&mut BufReader<&mut TcpStream>, &HttpHeader) -> Result<(FormData, JsonType),ParseError>>;
const BOUNDARY_PREFIX: &str = "boundary=";
const NEWLINE: &str = "\r\n";
const NAME_PREFIX: &str = "name=\"";
const CONTENT_TYPE_PREFIX: &str = "Content-Type: ";
const FILENAME_PREFIX: &str = "filename=\"";
const AMP: &str = "&";
const EQUAL: &str = "=";
const INVALID_BODY_STRING: &str = "Invalid request body!";

pub fn get_parser_by_content_type(content_type: ContentType) ->  BodyParser {
    match content_type {
        ContentType::JSON => Box::new(parse_json_body),
        ContentType::FormData => Box::new(parse_form_data),
        ContentType::FormUrlencoded => Box::new(parse_form_urlencoded),
        _ => panic!("Unsupported content type: {}",content_type.values())

    }
}

pub fn parse_form_data(buf_reader: &mut BufReader<&mut TcpStream>, header: &HttpHeader) -> Result<(FormData, JsonType),ParseError> {
    let content_length= header.get_content_length();
    let content_type = header.get_by_header(HttpHeadersEnum::ContentType);
    if content_type.is_none() {
        log("Failed to read content_type from http header!");
        return Ok((FormData::new(),JsonType::Null))
    }
    let content_type = content_type.unwrap();
    let boundary = content_type.split(BOUNDARY_PREFIX).last();
    if boundary.is_none() {
        log("Failed to find boundary prefix");
        return Err(ParseError::InvalidRequestError("Miss boundary for form-data!".to_string()));
    }
    let boundary = boundary.unwrap().trim().as_bytes();
    let mut form_data = FormData::new();
    match content_length {
        None => Ok((FormData::new(),JsonType::Null)),
        Some(content_length) => {
            let v8 = read_n_bytes(buf_reader, content_length);
            let groups = split_byte_vec_by_boundary(&v8, boundary);
            for group in groups {
                let mut lines = split_byte_vec_by_boundary(&group, NEWLINE.as_bytes());
                if lines.len() < 3 {
                    continue;
                }
                let header_information = split_byte_vec_by_boundary(&lines[1], SEMICOLON.as_bytes());
                if header_information.len() < 2 {
                    return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));
                }
                let name_line = header_information.get(1).unwrap();
                let name = String::from_utf8(copy_of_range(name_line, NAME_PREFIX.len(), name_line.len() - 1));
                if name.is_err() {
                    log("Failed to convert form-data's name");
                    return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()))
                }
                let name = name.unwrap();
                match header_information.len() {
                    2 => {
                        let value_line = lines.remove(3);
                        let value = String::from_utf8(value_line);
                        if let Err(err) = value {
                            log(err.to_string());
                            return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));
                        }
                        let value = value.unwrap();
                        form_data.put(name.as_str(), FormDataType::FString(value));
                    }
                    3 => {
                        let multipart_file_builder = MultipartFile::builder();
                        let filename_line = header_information.get(2).unwrap();
                        let filename = String::from_utf8(
                            copy_of_range(filename_line,FILENAME_PREFIX.len(),filename_line.len()-1)
                        );
                        if let Err(err) = filename {
                            log(err.to_string());
                            return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));
                        }
                        let filename = filename.unwrap();
                        let content_type_line = lines.get(2).unwrap();
                        let content_type = String::from_utf8(
                            copy_of_range(content_type_line, CONTENT_TYPE_PREFIX.len(),content_type_line.len())
                        );
                        if let Err(err) = content_type {
                            log(err.to_string());
                            return Err(ParseError::InvalidParseBodyError(INVALID_BODY_STRING.to_string()));
                        }
                        let content_type = content_type.unwrap().clone();
                        let mut bytes:Vec<u8> = Vec::new();
                        if lines.len() > 4 {
                            bytes = lines.remove(4);

                        }
                        let multipart_file = multipart_file_builder.content_type(content_type)
                            .filename(filename)
                            .bytes(bytes)
                            .build();
                        form_data.put(name,FormDataType::MultipartFile(multipart_file));
                    }
                    _ => {}
                }
            }
            Ok((form_data,JsonType::Null))
        }
    }
}

fn read_n_bytes(buf_reader: &mut BufReader<&mut TcpStream>, content_length: i32) -> Vec<u8> {
    let mut v8: Vec<u8> = Vec::new();
    let mut a8: [u8; 1024] = [0; 1024];
    let mut bytes_read: usize = 0;
    while bytes_read < content_length as usize {
        let read_result = buf_reader.read(&mut a8);
        match read_result {
            Ok(read_result) => { bytes_read += read_result; }
            Err(_err) => panic!("Failed to read request body!")
        }
        for u in a8 {
            if u != 0 {
                v8.push(u);
            } else {
                break
            }
        }
    }
    v8
}

fn split_byte_vec_by_boundary(input : &Vec<u8>, boundary: &[u8]) -> Vec<Vec<u8>> {
    let boundary_length = boundary.len();
    let mut result: Vec<Vec<u8>> = Vec::new();
    if boundary_length == 0 {
        panic!("Invalid boundary of form-data!")
    }
    let lps: Vec<usize> = build_lps(boundary);
    let mut cache = vec![0u8; boundary_length];
    let mut matched_prefix_length =0;
    let mut head = 0;
    let mut count = 0;
    let mut output:Vec<u8> = Vec::new();
    for byte in input.iter() {
        while matched_prefix_length > 0 && byte != &boundary[matched_prefix_length] {
            matched_prefix_length = lps[matched_prefix_length - 1];
        }
        if &boundary[matched_prefix_length] == byte {
            matched_prefix_length += 1;
        }
        cache[(head+count) % boundary_length] = *byte;
        count+=1;

        if matched_prefix_length == boundary_length {
            head = (head+count) % boundary_length;
            count = 0;
            result.push(output.clone());
            output.clear();
            matched_prefix_length = 0;
        } else {
            while count > matched_prefix_length {
                output.push(cache[head]);
                head = (head+1) % boundary_length;
                count -= 1;
            }
        }
    }
    result.push(output);
    result
}

fn build_lps(pattern: &[u8]) -> Vec<usize> {
    let pattern_length = pattern.len();
    let mut lps = vec![0;pattern_length];
    let mut pattern_index = 0;
    for i in 1..pattern_length {
        while pattern_index > 0 && pattern[i] != pattern[pattern_index] {
            pattern_index = lps[pattern_index-1];
        }
        if pattern[i] == pattern[pattern_index] {
            pattern_index += 1;
        }
        lps[i] = pattern_index;
    }
    lps
}

fn copy_of_range<T : Copy>(src: &Vec<T>, start: usize, end: usize) -> Vec<T> {
    let mut res: Vec<T> = Vec::new();
    for i in start..end {
        res.push(src[i])
    }
    res
}

pub fn parse_form_urlencoded(buf_reader: &mut BufReader<&mut TcpStream>, header: &HttpHeader) -> Result<(FormData,JsonType), ParseError> {
    let content_length = header.get_content_length();
    if content_length.is_none() {
        return Ok((FormData::new(),JsonType::Null))
    }
    let mut form_data = FormData::new();
    if let Some(content_length) = content_length {

        let raw_body = read_n_bytes(buf_reader, content_length);
        let data_pairs = split_byte_vec_by_boundary(&raw_body, AMP.as_bytes());
        for data_pair in data_pairs {
            let mut name_and_value = split_byte_vec_by_boundary(&data_pair, EQUAL.as_bytes());
            if name_and_value.len() > 2 { return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));}
            let value = String::from_utf8(name_and_value.pop().unwrap());
            if value.is_err() {
                log(value.err().unwrap().to_string());
                return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));
            }
            let name = String::from_utf8(name_and_value.pop().unwrap());
            if name.is_err() {
                log(name.err().unwrap().to_string());
                return Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()));
            }
            form_data.put(name.unwrap(), FormDataType::FString(value.unwrap()));
        }
    }
    Ok((form_data,JsonType::Null))
}

pub fn parse_json_body(buf_reader:&mut BufReader<&mut TcpStream>, header: &HttpHeader) -> Result<(FormData,JsonType),ParseError> {
    let content_length = header.get_content_length();
    if content_length.is_none() {
        return Ok((FormData::new(),JsonType::Null));
    }
    let raw_body = read_n_bytes(buf_reader, content_length.unwrap());
    let raw_body = String::from_utf8(raw_body);
    match raw_body {
        Ok(raw_body) => {
            let mut json_parser = JsonParser::new(raw_body.as_str());
            let parse_result = json_parser.parse();
            match parse_result {
                Ok(parse_result) => Ok((FormData::new(), parse_result)),
                Err(err) => Err(err)
            }
        }
        Err(err) => {
            log(err.to_string());
            Err(ParseError::InvalidRequestError(INVALID_BODY_STRING.to_string()))
        }
    }
}