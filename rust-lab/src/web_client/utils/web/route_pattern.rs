use std::fmt::{Debug, Formatter};
use crate::web_client::enums::{RequestHandler, PathType};
use crate::web_client::enums::PathType::{DYNAMIC, STATIC};
use crate::web_client::utils::pair::Pair;
pub struct RoutePattern {
    pub(crate) pattern: String,
    pub(crate) path_type: PathType,
    pub(crate) segments: Vec<Pair<String, String>>,
    pub(crate) handler: Option<RequestHandler>,
}

impl RoutePattern {
    pub fn new(pattern: &str, path_type: PathType, segments: Vec<Pair<String, String>>) -> RoutePattern {
        RoutePattern{
            pattern: pattern.to_string(),
            path_type,
            segments,
            handler: None,
        }
    }
    pub fn parse(path: &str) -> RoutePattern {
        let splits:Vec<&str> = path.split("/").collect();
        let mut segments: Vec<Pair<String, String>> = Vec::new();
        let mut variable_exists = false;
        for split in splits {
            if split.starts_with("{") && split.ends_with("}") {
                variable_exists = true;
                segments.push(Pair::new("*".to_string(),split[1..split.len()-1].to_string()));
            }else{
                segments.push(Pair::new(split.to_string(),split.to_string()));
            }
        }
        Self::new(
            path,
             if variable_exists { DYNAMIC } else { STATIC },
            segments
        )
    }
}

impl Debug for RoutePattern {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("RoutePattern")
            .field("pattern", &self.pattern)
            .field("path_type", &self.path_type)
            .field("segments", &self.segments)
            .field("handler", &"<handler>")
            .finish()
    }
}