use crate::web_client::web_client::HttpRequest;

pub fn health_check(http_request:&HttpRequest) -> Result<String,std::io::Error> {
    Ok("health check".to_string())
}

pub fn question_params(http_request:&HttpRequest) -> Result<String,std::io::Error> {
    let map = &http_request.params;
    Ok(format!("page: {}, size: {}",map.get("page").unwrap_or(&"0".to_string()),map.get("size").unwrap_or(&"0".to_string())))
}