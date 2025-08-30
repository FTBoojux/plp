mod test{
    use std::collections::{BTreeMap, HashMap};
    use std::fmt::Debug;
    use std::fs::File;
    use std::io::Write;
    use std::path::Path;
    use std::sync::{Mutex, OnceLock, RwLock};
    use std::time::{SystemTime, UNIX_EPOCH};
    use crate::web_client::utils::json::bson::FromJson;
    use crate::web_client::utils::json::bson::FromJsonOption;
    use crate::web_client::utils::json::bson::JsonType;
    use json_derive::FromJson;
    use crate::web_client::health_check::{health_check, path_variable};
    use crate::web_client::utils::fog::fog::log;
    use crate::web_client::web_client::{RequestMatchResult, WebClientBuilder};
    use crate::web_client::enums::HttpMethod;
    #[derive(FromJson, Debug)]
    struct User{
        username: String,
        password: String,
        address: Option<String>,
    }
    static DATABASE: OnceLock<RwLock<HashMap<&str, &str>>> = OnceLock::new();
    fn db() -> &'static RwLock<HashMap<&'static str, &'static str>> {
        DATABASE.get_or_init(||RwLock::new(HashMap::from([
            ("Boojux", "dadwa"),
            ("Admin","Admin"),
            ("dawdwa","jnmiohnoi"),
        ])))
    }
    fn login(http_request: RequestMatchResult)-> Result<String,std::io::Error>{
        let option = http_request.request.request_body;
        if let Some(user) = option{
            let result = User::from_json(user);
            match result {
                Ok(user) => {
                    if let Some(pass) = db().read().unwrap().get(user.username.as_str()){
                      if pass.eq(&user.password) {
                          return Ok("{\"msg\":\"You have logged in successfully!\"}".to_string());
                      }
                    };
                    Ok("Username or password is incorrect!".to_string())
                }
                Err(e) => {log(e);Ok("lack necessary information to log in!".to_string())}
            }
        }else {
            Ok("lack necessary information to log in!".to_string())
        }
    }
    fn form_data_handler(match_result: RequestMatchResult) -> Result<String,std::io::Error> {
        let request = match_result.request;
        let form_data= request.form_data;
        let username = form_data.get_string("username");
        match username {
            None => Ok("{\"msg\":\"this username is invalid!\"}".to_string()),
            Some(username) => {
                if db().read().unwrap().contains_key(username.as_str()) {
                    Ok("{\"msg\":\"this username had been used!\"}".to_string())
                } else {
                    Ok("{\"msg\":\"this username is valid!\"}".to_string())
                }
            }
        }
    }
    fn form_urlencoded_handler(request_match_result: RequestMatchResult) -> Result<String,std::io::Error> {
        let form_data = request_match_result.request.form_data;
        let num = form_data.get_i32("num");
        match num {
            None => Ok("{\"res\": null}".to_string()),
            Some(num) => Ok(format!("{{\"res\": {} }}",num+1)),
        }
    }
    fn file_upload_handler(request_match_result: RequestMatchResult) -> Result<String,std::io::Error> {
        let form_data = request_match_result.request.form_data;
        let multipart_file = form_data.get_multipart_file("file");
        match multipart_file {
            None => {}
            Some(multipart_file) => {
                let filename = &multipart_file.filename;
                let suffix = filename.split(".").last().unwrap();
                let file_path = format!("D:\\tmp\\\\{}.{}",
                                        SystemTime::now().duration_since(UNIX_EPOCH).unwrap().as_millis(),
                                        suffix);
                let path = Path::new(file_path.as_str());
                let file = File::create(path);
                if file.is_err() { panic!() }
                let result= file?.write(&multipart_file.bytes);
                if result.is_err() { panic!() }
            }
        }
        Ok("ok".to_string())
    }
    #[test]
    fn login_with_request(){
        let client = WebClientBuilder::new()
            .port(8000)
            .backlog(300)
            .threads(11)
            .route(HttpMethod::GET, "/health", Box::new(health_check)).unwrap()
            .route(HttpMethod::GET, "/{name}/path", Box::new(path_variable)).unwrap()
            .route(HttpMethod::POST, "/login", Box::new(login)).unwrap()
            .route(HttpMethod::POST,"/formData",Box::new(form_data_handler)).unwrap()
            .build()
            .unwrap().listen()
            ;
    }
    #[test]
    fn request_with_different_content_type(){
        WebClientBuilder::new()
            .port(8000)
            .backlog(300)
            .threads(11)
            .route(HttpMethod::GET, "/health", Box::new(health_check)).unwrap()
            .route(HttpMethod::GET, "/{name}/path", Box::new(path_variable)).unwrap()
            .route(HttpMethod::POST, "/login", Box::new(login)).unwrap()
            .route(HttpMethod::POST,"/formData",Box::new(form_data_handler)).unwrap()
            .route(HttpMethod::POST, "/formUrlencoded",Box::new(form_urlencoded_handler)).unwrap()
            .route(HttpMethod::POST,"/fileUpload",Box::new(file_upload_handler)).unwrap()
            .build()
            .unwrap().listen()
        ;
    }
    #[test]
    fn test_http_enum(){
        println!("{}", HttpMethod::GET.value());
        println!("{:?}", "POST".eq(HttpMethod::POST.value()));
    }
}
