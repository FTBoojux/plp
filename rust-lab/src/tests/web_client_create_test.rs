#[cfg(test)]
mod tests {
    use crate::web_client::health_check::health_check;

    #[test]
    fn test_create_web_client_with_unused_port_successful(){
        let client = crate::web_client::web_client::WebClientBuilder::new()
            .port(8000)
            .route("health_check",Box::new(health_check)).unwrap()
            .build()
            .unwrap().listen()
            ;
    }
}