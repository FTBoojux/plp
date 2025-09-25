use crate::web_client::enums::RequestHandler;
use crate::web_client::utils::web::route_tree::MatchResult;
use crate::web_client::web_client::{HttpRequest, HttpResponse, RequestMatchResult};

pub fn first_middleware(http_request: &mut HttpRequest, http_response: &mut HttpResponse) -> bool {
    let header = &http_request.headers;
    let cnt = header.get("cnt");
    match cnt {
        None => http_request.headers.insert("cnt",String::from("1")),
        Some(cnt) => {
            http_response.status_code = 403;
            http_response.response_phrase = "Forbidden".to_string();
            return false;
        },
    };
    true
}

pub fn second_middleware(http_request: &mut HttpRequest, http_response: &mut HttpResponse) -> bool {
    let header = &mut http_request.headers;
    match header.get_as_u32("cnt") {
        None => false,
        Some(cnt) => {
            header.insert("cnt",(cnt+1).to_string());
            true
        }
    }
}
pub fn illegal_middleware(http_request: &mut HttpRequest, http_response: &mut HttpResponse) -> bool {
    if http_response.body.contains("illegal information") {
        http_response.status_code = 401;
        http_response.body = String::from("Unavailable For Legal Reasons");
        return false;
    }
    true
}

pub fn middleware_count(http_request:RequestMatchResult) -> Result<String,std::io::Error> {
    let cnt = http_request.request.headers.get_as_u32("cnt");
    if let Some(cnt) = cnt {
        return Ok(cnt.to_string())
    }
    Ok("".to_string())
}

pub fn illegal_information_handler(match_result: RequestMatchResult) ->Result<String, std::io::Error> {
    Ok(String::from("illegal information"))
}