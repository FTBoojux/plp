mod test{
    use std::collections::{BTreeMap, HashMap};
    use std::sync::{Mutex, OnceLock, RwLock};
    use crate::web_client::utils::json::bson::FromJson;
    use crate::web_client::utils::json::bson::FromJsonOption;
    use crate::web_client::utils::json::bson::JsonType;
    use json_derive::FromJson;
    use crate::web_client::health_check::{health_check, path_variable};
    use crate::web_client::utils::fog::fog::log;
    use crate::web_client::web_client::{HttpRequest, RequestMatchResult, WebClientBuilder};

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
                          return Ok("You have logged in successfully!".to_string());
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
    #[test]
    fn login_with_request(){
        let client = WebClientBuilder::new()
            .port(8000)
            .backlog(300)
            .threads(11)
            .route("/health",Box::new(health_check)).unwrap()
            // .route("/page_query",Box::new(question_params)).unwrap()
            .route("/{name}/path",Box::new(path_variable)).unwrap()
            // .route("/{id}/info",Box::new(another_path_variable)).unwrap()
            .route("/login",Box::new(login)).unwrap()
            .build()
            .unwrap().listen()
            ;
    }
}
