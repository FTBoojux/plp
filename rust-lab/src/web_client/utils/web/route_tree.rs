use crate::web_client::enums::RequestHandler;
use crate::web_client::utils::web::route_pattern::RoutePattern;
use std::collections::{HashMap, VecDeque};
use std::fmt::{Debug, Formatter};
use std::ops::Deref;
struct MatchCandidate<'a> {
    route_tree: &'a RouteTree,
    path_variables: HashMap<String, String>,
}
// #[derive(Debug)]
pub struct MatchResult<'a>{
    pub request_handler: &'a RequestHandler,
    pub path_variables: HashMap<String, String>,
}

impl Debug for MatchResult<'_> {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("MatchResult")
        .field("request_handler", &"request handler")
        .field("path_variables", &self.path_variables)
        .finish()
    }
}
pub struct RouteTree{
    segment: String,
    path: String,
    children: HashMap<String, RouteTree>,
    request_handler: Option<RequestHandler>
}

impl RouteTree{
    pub fn new(segment: &str, path: &str) -> RouteTree{
        RouteTree{
            segment: segment.to_string(),
            children : HashMap::new(),
            path: path.to_string(),
            request_handler: None
        }
    }
    pub fn add_route(&mut self, mut route: RoutePattern){
        let mut node = self;
        let segments = route.segments;
        for i in 0..segments.len(){
            let segment = &segments[i];
            if !node.children.contains_key(&segment.first) {
               node.children.insert(segment.first.clone(), RouteTree::new(&segment.first, &segment.second));
            }
            if i+1 == segments.len() {
                if let Some(handler) = route.handler.take(){
                    node.children.get_mut(&segment.first).unwrap().request_handler = Some(handler);
                }
                ()
            }
            node = node.children.get_mut(&segment.first).unwrap();
        }
    }
    pub fn find(&self, path: &str) -> Option<MatchResult> {
        let segments:Vec<&str> = path.split("/").collect();
        let mut queue:VecDeque<MatchCandidate> = VecDeque::new();
        queue.push_back(MatchCandidate{
            route_tree: self,
            path_variables: HashMap::new()
        });
        for segment in segments {
            let mut next_level:VecDeque<MatchCandidate> = VecDeque::new();
            while !queue.is_empty() {
                let candidate = queue.pop_front().unwrap();
                let child = candidate.route_tree.children.get(&segment.to_string());
                if let Some(child) = child {
                    next_level.push_back(MatchCandidate{
                        route_tree: child,
                        path_variables: candidate.path_variables.clone(),
                    })
                }
                let child = candidate.route_tree.children.get("*");
                if let Some(child) = child {
                    let mut path_variables = candidate.path_variables.clone();
                    path_variables.insert(child.path.clone(), segment.to_string());
                    next_level.push_back(MatchCandidate{
                        route_tree: child,
                        path_variables
                    })
                }
            }
            queue = next_level;
        }
        while !queue.is_empty() {
            let candidate = queue.pop_front().unwrap();
            let handler = &candidate.route_tree.request_handler;
            if let Some(handler) = handler {
                let option = Some(MatchResult {
                    request_handler: handler,
                    path_variables: candidate.path_variables.clone(),
                });
                return option;
            }
        }
        None
    }
}

impl Debug for RouteTree{
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("RouteTree")
        .field("segment", &self.segment)
        .field("children", &self.children)
            .field("path", &self.path)
            .finish()
    }
}

#[cfg(test)]
mod test{
    use crate::web_client::health_check::health_check;
    use crate::web_client::utils::web::route_pattern::RoutePattern;
    use crate::web_client::utils::web::route_tree::RouteTree;

    #[test]
    pub fn match_path_and_variable(){
        let mut route_tree = RouteTree::new("", "");
        let mut pattern1 = RoutePattern::parse("/{pathVariable1}/{pathVariable2}/path");
        pattern1.handler = Some(Box::new(health_check));
        route_tree.add_route(pattern1);

        let mut pattern2 = RoutePattern::parse("/{pathVariable}/path");
        pattern2.handler = Some(Box::new(health_check));
        route_tree.add_route(pattern2);

        let mut pattern3 = RoutePattern::parse("/path/{pathVariable}");
        pattern3.handler = Some(Box::new(health_check));
        route_tree.add_route(pattern3);

        let mut pattern4 = RoutePattern::parse("/{pathVariable1}/path/{pathVariable2}/path");
        pattern4.handler = Some(Box::new(health_check));
        route_tree.add_route(pattern4);

        let option = route_tree.find("/1/2/path");
        assert!(option.is_some());
        let option = option.unwrap();
        println!("{:?}", option);

        let option = route_tree.find("/1/path");
        assert!(option.is_some());
        let option = option.unwrap();
        println!("{:?}", option);

        let option = route_tree.find("/path/1");
        assert!(option.is_some());
        let option = option.unwrap();
        println!("{:?}", option);

        let option = route_tree.find("/path/path/path/path");
        assert!(option.is_some());
        let option = option.unwrap();
        println!("{:?}", option);
        ()
    }
}