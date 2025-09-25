use crate::web_client::utils::web::multipart::MultipartFile;
use std::collections::HashMap;

#[derive(Debug,Clone)]
pub enum FormDataType {
    FString(String),
    Int(i32),
    Boolean(bool),
    Float(f64),
    MultipartFile(MultipartFile),
}
#[derive(Debug,Clone)]
pub struct FormData {
    data: HashMap<String, Vec<FormDataType>>
}

impl FormData {
    pub fn new() -> Self {
        Self{
            data: HashMap::new()
        }
    }
    pub fn put(&mut self, key: impl Into<String>, value: FormDataType) {
        let key = key.into();
        if !self.data.contains_key(&key) {
            self.data.insert(key.clone(),Vec::new());
        }
        self.data.get_mut(&key).unwrap().push(value)
    }
    pub fn put_string(&mut self, key: &str, value: impl Into<String>) {
        self.put(key,FormDataType::FString(value.into()))
    }
    pub fn put_int(&mut self, key: &str, value: i32) {
        self.put(key,FormDataType::Int(value))
    }
    pub fn put_bool(&mut self, key: &str, value: bool) {
        self.put(key,FormDataType::Boolean(value))
    }
    pub fn put_float(&mut self, key: &str, value: f64) {
        self.put(key, FormDataType::Float(value))
    }
    pub fn put_multipart_file(&mut self, key: &str, value: MultipartFile) {
        self.put(key, FormDataType::MultipartFile(value))
    }
    pub fn get_strings(&self, key: &str) -> Option<Vec<String>> {
        let values = self.data.get(key);
        match &values {
            None => None,
            Some(values) => {
                let mut vec:Vec<String> = Vec::new();
                for value in values.iter() {
                    match value {
                        FormDataType::FString(str) => {
                            vec.push(str.clone());
                        }
                        FormDataType::Float(float) => {
                            vec.push(float.to_string());
                        }
                        FormDataType::Int(int) => {
                            vec.push(int.to_string());
                        }
                        FormDataType::Boolean(bool) => {
                            vec.push(bool.to_string());
                        }
                        _ => {
                            panic!("Failed to convert {} to String!",key);
                        }
                    }
                }
                Some(vec)
            }
        }
    }
    pub fn get_string(&self, key: &str) -> Option<String> {
        let values = self.data.get(key);
        match &values {
            None => None,
            Some(values) => {
                let first_element = values.first();
                match first_element {
                    None => None,
                    Some(for_data) => {
                        match for_data {
                            FormDataType::FString(string) => Some(string.clone()),
                            FormDataType::Int(int) => Some(int.to_string()),
                            FormDataType::Boolean(bool) => Some(bool.to_string()),
                            FormDataType::Float(float) => Some(float.to_string()),
                            FormDataType::MultipartFile(_) => None,
                        }
                    }
                }
            }
        }
    }
    pub fn get_i32s(&self, key: &str) -> Option<Vec<i32>> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                let mut vec: Vec<i32> = Vec::new();
                for value in values.iter() {
                    match value {
                        FormDataType::FString(string) => {
                            let result = string.parse::<i32>();
                            match result {
                                Ok(int) => { vec.push(int) }
                                Err(err) => { panic!("{}", err) }
                            }
                        },
                        FormDataType::Boolean(bool) => {
                            vec.push(if *bool { 1 } else { 0 });
                        },
                        FormDataType::Float(float) => {
                            vec.push(*float as i32);
                        },
                        FormDataType::Int(int) => {
                            vec.push(*int);
                        },
                        FormDataType::MultipartFile(_) => {
                            panic!("Failed to convert to i32 for key {}", key)
                        }
                    }
                }
                Some(vec)
            }
        }
    }
    pub fn get_i32(&self, key: &str) -> Option<i32> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                match values.first() {
                    None => None,
                    Some(value) => {
                        match value {
                            FormDataType::FString(string) => {
                                let parse_result = string.parse::<i32>();
                                match parse_result {
                                    Ok(int) => Some(int),
                                    Err(err) => panic!("{}", err)
                                }
                            }
                            FormDataType::Int(int) => Some(*int),
                            FormDataType::Float(float) => Some(*float as i32),
                            _ => panic!("Failed to convert {} to i32!",key)
                        }
                    }
                }
            }
        }
    }
    pub fn get_f64s(&self, key: &str) -> Option<Vec<f64>> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                let mut vec: Vec<f64> = Vec::new();
                for value in values.iter() {
                    match value {
                        FormDataType::FString(string) => {
                            let result = string.parse::<f64>();
                            match result {
                                Ok(f64) => { vec.push(f64) }
                                Err(err) => { panic!("{}", err) }
                            }
                        },
                        FormDataType::Boolean(bool) => {
                            vec.push(if *bool { 1.0 } else { 0.0 });
                        },
                        FormDataType::Float(float) => {
                            vec.push(*float);
                        },
                        FormDataType::Int(int) => {
                            vec.push(*int as f64);
                        },
                        FormDataType::MultipartFile(_) => {
                            panic!("Failed to convert to f64 for key {}", key)
                        }
                    }
                }
                Some(vec)
            }
        }
    }
    pub fn get_f64(&self, key: &str) -> Option<f64> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                match values.first() {
                    None => None,
                    Some(value) => {
                        match value {
                            FormDataType::FString(string) => {
                                let parse_result = string.parse::<f64>();
                                match parse_result {
                                    Ok(f64) => Some(f64),
                                    Err(err) => panic!("{}", err)
                                }
                            }
                            FormDataType::Int(int) => Some(*int as f64),
                            FormDataType::Float(float) => Some(*float),
                            _ => panic!("Failed to convert {} to i32!",key)
                        }
                    }
                }
            }
        }
    }
    pub fn get_bools(&self, key : &str) -> Option<Vec<bool>> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                let mut vec: Vec<bool> = Vec::new();
                for value in values.iter() {
                    match value {
                        FormDataType::FString(string) => {
                            match string.as_str() {
                               "true" => vec.push(true) ,
                                "false" => vec.push(false),
                                _ => panic!("Failed to convert {} to bool!",key)
                            }
                        }
                        FormDataType::Int(i32) => {
                            vec.push(if *i32 > 0 { true } else { false })
                        }
                        FormDataType::Boolean(bool) => {
                            vec.push(*bool);
                        }
                        FormDataType::Float(float) => {
                            vec.push(if *float > 0.0 { true } else { false })
                        }
                        _ => vec.push(false)
                    }
                }
                Some(vec)
            }
        }
    }
    pub fn get_bool(&self, key : &str) -> Option<bool> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                let value = values.first();
                match value {
                    None => None,
                    Some(value) => {
                        match value {
                            FormDataType::FString(string) => {
                                match string.as_str() {
                                    "true" => Some(true),
                                    "false" => Some(false),
                                    _ => panic!("Failed to convert {} to bool!", key)
                                }
                            }
                            FormDataType::Int(int) => Some(*int > 0),
                            FormDataType::Boolean(bool) => Some(*bool),
                            FormDataType::Float(float) => Some(*float > 0.0),
                            _ => Some(false),
                        }
                    }
                }
            }
        }
    }
    pub fn get_multipart_files(&self, key : &str) -> Option<Vec<&MultipartFile>> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                let mut vec: Vec<&MultipartFile> = Vec::new();
                for value in values.iter() {
                    if let FormDataType::MultipartFile(multipart_file) = value {
                        vec.push(multipart_file);
                    }
                }
                Some(vec)
            }
        }
    }
    pub fn get_multipart_file(&self, key : &str) -> Option<&MultipartFile> {
        let values = self.data.get(key);
        match values {
            None => None,
            Some(values) => {
                if let Some(value) = values.first() {
                    if let FormDataType::MultipartFile(multipart_file) = value {
                        return Some(multipart_file)
                    }
                }
                None
            }
        }
    }
}

mod test {
    use crate::web_client::utils::web::form_data::FormData;

    #[test]
    fn query_different_type_data_from_form_data(){
        let mut form_data = FormData::new();
        form_data.put_string("name","Boojux");
        form_data.put_string("sex","male");
        form_data.put_int("age",1);
        form_data.put_string("height","172.3");
        form_data.put_string("categories","meat");
        form_data.put_string("categories", "fish");

        let name = form_data.get_string("name");
        assert!(name.is_some() && name.unwrap().eq("Boojux"));

        let sex = form_data.get_string("sex");
        assert!(sex.is_some() && sex.unwrap().eq("male"));

        let age = form_data.get_i32("age");
        assert!(age.is_some() && age.unwrap() == 1);

        let height = form_data.get_f64("height");
        assert!(height.is_some() && height.unwrap() == 172.3);

        let mut categories = form_data.get_strings("categories");
        assert!(categories.is_some());
        let category = categories.take().unwrap();
        assert!(category[0].eq("meat") && category[1].eq("fish"));
    }
}