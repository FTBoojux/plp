use crate::web_client::web_client::{HttpRequest, RequestMatchResult};

pub fn health_check(http_request:&RequestMatchResult) -> Result<String,std::io::Error> {
    Ok("health check".to_string())
}

pub fn question_params(http_request:&RequestMatchResult) -> Result<String,std::io::Error> {
    let map = &http_request.request.params;
    Ok(format!("page: {}, size: {}",map.get("page").unwrap_or(&"0".to_string()),map.get("size").unwrap_or(&"0".to_string())))
}

pub fn path_variable(http_request:&RequestMatchResult) -> Result<String,std::io::Error> {
    let map = &http_request.path_variables;
    Ok(format!("name: {}", map.get("name").unwrap_or(&"0".to_string())))
}

pub fn path_variable_2(http_request:&RequestMatchResult) -> Result<String,std::io::Error> {
    let map = &http_request.path_variables;
    Ok(format!("name: {}", map.get("name2").unwrap_or(&"0".to_string())))
}

pub fn another_path_variable(http_request:&RequestMatchResult) -> Result<String,std::io::Error> {
    let map = &http_request.path_variables;
    Ok(format!("id: {}", map.get("id").unwrap_or(&"0".to_string())))
}