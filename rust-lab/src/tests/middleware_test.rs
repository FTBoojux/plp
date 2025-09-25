mod test{
    use crate::web_client::example::first_middleware;
    use crate::web_client::web_client::{ WebClientBuilder};

    #[test]
    fn add_pre_middleware_to_web_client(){
        WebClientBuilder::new().port(8000)
            .add_pre_handler(Box::new(first_middleware));
    }
    #[test]
    fn add_post_middleware_to_web_client(){
        WebClientBuilder::new().port(8000)
            .add_post_handler(Box::new(first_middleware));
    }


}