#[cfg(test)]
mod tests {
    use std::collections::HashMap;
    use crate::web_client::health_check::health_check;

    #[test]
    fn test_create_web_client_with_unused_port_successful(){
        let client = crate::web_client::web_client::WebClientBuilder::new()
            .port(8000)
            .route("get","/health_check",Box::new(health_check)).unwrap()
            .build()
            .unwrap().listen()
            ;
    }
    #[test]
    fn test_use_wildcard_as_key_in_hashmap(){
        let mut map:HashMap<String, String> = HashMap::new();
        map.insert("*".to_string(),"wildcard".to_string());
        map.insert("=".to_string(),"equal".to_string());
        let option = map.get("*");
        print!("{}", format!("res: {}", option.unwrap().as_str()));
        ()
    }
}