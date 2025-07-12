use crate::web_client::web_client::HttpRequest;

pub fn health_check(http_request:  &HttpRequest) -> Result<String,std::io::Error> {
    Ok("health check".to_string())
}

pub fn query_page_params(http_request:  &HttpRequest) -> Result<String,std::io::Error> {
    let params = &http_request.params;
    let page = params.get("page");
    let size = params.get("size");
    Ok(format!("page: {}, size: {}", page.unwrap_or(&"none".to_string()), size.unwrap_or(&"none".to_string())))
}