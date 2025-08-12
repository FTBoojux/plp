use crate::web_client::error::err::ParseError;
use crate::web_client::error::err::ParseError::InvalidParseBodyError;
use std::collections::HashMap;
use std::str::Chars;

#[derive(Debug, PartialEq)]
enum JsonType {
    Number(f64),
    JString(String),
    Boolean(bool),
    Array(Vec<JsonType>),
    Object(HashMap<String, JsonType>),
    Null,
}

struct JsonParser<'a> {
    json_it: Chars<'a>,
    index: u32,
    peek: Option<char>,
}

trait FromJson: Sized{
    fn from_json(value: JsonType) -> Result<Self, String>;
}

trait FromJsonOption: Sized{
    fn from_json_optional(value: Option<JsonType>) -> Result<Self, String>;
}

impl<'a> JsonParser<'a> {
    pub fn new(json_str: &'a str) -> Self {
        let mut chars = json_str.chars();
        let top = chars.next();
        JsonParser {
            json_it: chars,
            index: 0,
            peek: top,
        }
    }
    pub fn parse(&mut self) -> Result<JsonType, ParseError> {
        self.skip_whitespace();
        self.parse_value()
    }

    fn parse_value(&mut self) -> Result<JsonType, ParseError> {
        self.skip_whitespace();
        match self.peek(){
            '{' => self.parse_object(),
            '[' => self.parse_array(),
            '"' =>  self.parse_string(),
            't' | 'f' => self.parse_boolean(),
            'n' => self.parse_null(),
            _ => {
                if self.peek.unwrap().is_digit(10) || self.peek.unwrap() == '-' {
                    return self.parse_number();
                }
                Err(InvalidParseBodyError(format!(
                    "Unknow character : {}",
                    self.peek()
                )))
            }
        }
    }
    fn parse_object(&mut self) -> Result<JsonType, ParseError> {
        let mut object  = HashMap::new();
        if self.peek() == '{' {
            self.pop();
        }
        self.skip_whitespace();
        if self.peek() == '}' {
            self.pop();
            return Ok(JsonType::Object(object));
        }
        loop {
            self.skip_whitespace();
            if let Ok(JsonType::JString(key)) = self.parse_string(){
                self.skip_whitespace();
                if self.peek() == ':' {
                    self.pop();
                } else {
                    return Err(InvalidParseBodyError(format!("Unexpected token at index: {}", self.index)));
                }
                let value = self.parse_value();
                object.insert(key, value?);
                self.skip_whitespace();
                match self.peek() {
                    ',' => {
                        self.pop();
                        self.skip_whitespace();
                    },
                    '}' => {
                        self.pop();
                        return Ok(JsonType::Object(object));
                    },
                    _ => return Err(InvalidParseBodyError(format!("Expected '}}' or ',' at index: {}", self.index)))
                }
            }else {
                return Err(InvalidParseBodyError(format!("Expected string as ket at index: {}", self.index)))
            }
        }
    }
    fn parse_array(&mut self) -> Result<JsonType, ParseError> {
        let mut array : Vec<JsonType>= Vec::new();
        self.pop();
        self.skip_whitespace();
        if self.peek() == ']' {
            return Ok(JsonType::Array(array));
        }
        loop {
            array.push(self.parse_value()?);
            self.skip_whitespace();
            if self.peek() == ',' {
                self.pop();
                self.skip_whitespace();
            }else if self.peek() == ']' {
                self.pop();
                self.skip_whitespace();
                break;
            }else {
                return Err(InvalidParseBodyError(format!("Expected ',' or ']' at index {}", self.index)));
            }
        }
        Ok(JsonType::Array(array))
    }
    fn parse_string(&mut self) -> Result<JsonType, ParseError> {
        if self.peek() == '"' {
            self.pop();
        }
        let mut string = String::new();
        loop {
            let char = self.peek();
            match char {
                '"' =>{
                    self.pop();
                    return Ok(JsonType::JString(string));
                },
                '\\' => {
                    self.pop();
                    let next = self.peek();
                    match next {
                        '"' | '\\' | '/' => string.push(next),
                        'b' => string.push('\u{0008}'),
                        'f' => string.push('\u{000C}'),
                        'n' => string.push('\n'),
                        'r' => string.push('\r'),
                        't' => string.push('\t'),
                        'u' => {
                            let mut unicode = 0u32;
                            for _ in 0..4 {
                                unicode = (unicode << 4) + self.pop().to_digit(16).ok_or(InvalidParseBodyError(format!("Unexpected unicode char at index: {}", self.index)))?;
                            }
                            let result = char::from_u32(unicode).ok_or(InvalidParseBodyError(format!("Unexpected unicode char at index: {}", self.index)))?;
                            string.push(result);
                        },
                        _ => return Err(InvalidParseBodyError(format!("Invalid unicode at index: {}", self.index)))
                    }
                },
                _ => string.push(self.pop()),
            }
        }
    }
    fn parse_boolean(&mut self) -> Result<JsonType, ParseError> {
        match self.peek() {
            't' => {
                if self.start_with("true") {
                    return Ok(JsonType::Boolean(true));
                }
                Err(InvalidParseBodyError(format!("Unexpected bool char at index: {}", self.index)))
            } ,
            'f' => {
                if self.start_with("false") {
                    return Ok(JsonType::Boolean(false));
                }
                Err(InvalidParseBodyError(format!("Unexpected bool char at index: {}", self.index)))
            }
            _ => Err(InvalidParseBodyError(format!("Unexpected bool char at index: {}", self.index)))
        }
    }
    fn parse_null(&mut self) -> Result<JsonType, ParseError> {
        if self.start_with("null") {
            return Ok(JsonType::Null)
        }
        Err(InvalidParseBodyError(format!("Invalid token at index: {} , expect null.", self.index)))
    }
    fn parse_number(&mut self) -> Result<JsonType, ParseError> {
        let mut string = String::new();
        if self.peek() == '-' {
            self.pop();
            string.push('-');
        }
        while self.peek().is_digit(10) {
            string.push(self.pop());
        }
        let is_decimal = self.peek() == '.';
        if is_decimal { string.push(self.pop()); }
        while self.peek().is_digit(10) {
            string.push(self.pop());
        }
        if self.peek() == 'e' || self.peek() == 'E' {
            string.push(self.pop());
            if self.peek() == '+' || self.peek() == '-' {
                string.push(self.pop())
            }
            if !self.peek().is_digit(10) {
                return Err(InvalidParseBodyError(format!("Invalid number at index: {}", self.index)));
            }
            while self.peek().is_digit(10) {
                string.push(self.pop());
            }
        }
        let number = string.parse().unwrap();
        Ok(JsonType::Number(number))
    }

    fn pop(&mut self) -> char {
        let option = self.peek;
        self.peek = self.json_it.next();
        self.index += 1;
        if option.is_some() {
            option.unwrap()
        }else {
            panic!("{}", format!("Unexpected end at index : {}",self.index));
        }
    }
    fn start_with(&mut self, prefix: &str) -> bool {
        for char in prefix.chars() {
            if self.peek() == char {
                self.pop();
            }else {
                return false;
            }
        }
        true
    }

    pub fn skip_whitespace(&mut self) {
        while self.peek().is_whitespace() || self.peek() == '\n'{
            self.pop();
        }
    }
    pub fn peek(&self) -> char {
        self.peek.unwrap()
    }
}

impl FromJson for String {
    fn from_json(value: JsonType) -> Result<Self, String> {
        match value {
            JsonType::JString(string) => Ok(string),
            _ => Err(format!("Expected String but got: {:?}", value))
        }
    }
}

impl FromJson for f64 {
    fn from_json(value: JsonType) -> Result<Self, String> {
        match value {
            JsonType::Number(n) => Ok(n),
            _ => Err(format!("Expected number but got: {:?}", value))
        }
    }
}

impl FromJson for bool {
    fn from_json(value: JsonType) -> Result<Self, String> {
        match value {
            JsonType::Boolean(b) => Ok(b),
            _ => Err(format!("Expected bool but got : {:?}", value))
        }
    }
}
impl<T: FromJson> FromJson for Vec<T> {
    fn from_json(value: JsonType) -> Result<Self, String> {
        match value {
            JsonType::Array(array) => {
                array.into_iter().map(
                    T::from_json
                ).collect::<Result<Vec<_>, String>>()
            },
            _ => Err(format!("Expected array but got : {:?}", value))
        }
    }
}

impl<T: FromJson> FromJson for Option<T> {
    fn from_json(value: JsonType) -> Result<Self, String> {
        match value {
            JsonType::Null => Ok(None),
            other => T::from_json(other).map(Some)
        }
    }
}

impl<T: FromJson> FromJsonOption for Option<T> {
    fn from_json_optional(value: Option<JsonType>) -> Result<Self, String> {
        match value {
            // 缺省字段
            // missing fields
            None => Ok(None),
            Some(JsonType::Null) => Ok(None),
            Some(other) => T::from_json(other).map(Some)
        }
    }
}

#[cfg(test)]
mod tests{
    use crate::web_client::utils::json::bson::FromJsonOption;
    use crate::web_client::utils::json::bson::FromJson;
    use crate::web_client::utils::json::bson::{JsonParser, JsonType};
    use json_derive::FromJson;
    #[test]
    fn convert_one_line_json_string() {
        let json_string = "
            \"id\": \"12\\n3\",
            \"price\": \"123.45\",
            \"quantity\": -10,
            \"ratio\": 1.2e-5,
            \"nullable\": null,
            \"na_me\": \"na_me\"
        ";
        let string = r#"{ "name":"Ada", "age":27, "tags":["rust","json"] }"#;
        let mut parse = JsonParser::new(string);
        let result = parse.parse();
        match result {
            Ok(result) => {
                println!("{:?}", result)
            }
            Err(err) => {
                println!("{:?}", err)
            }
        }
    }
    #[test]
    fn convert_multi_line_json_string() {
         let string = r###"{
            "id": "12\n3",
            "price": "123.45",
            "quantity": -10,
            "ratio": 1.2e-5,
            "nullable": null,
            "na_me": {"item":"item1"},
            "emoji": "❤"
            }
        "###;
        let mut parse = JsonParser::new(string);
        let result = parse.parse();
        match result {
            Ok(result) => {
                println!("{:?}", result)
            }
            Err(err) => {
                println!("{:?}", err)
            }
        }
    }
    #[test]
    fn using_from_json_macro(){
        let string = r#"
        {
            "name": "Boojux",
            "age": 24,
            "address" : "Bake Street 2B",
            "null_address" : null
        }
        "#;
        let mut parser = JsonParser::new(string);
        let result = parser.parse();
        match result {
            Ok(json_type) => {
                let result1 = User::from_json(json_type);
                println!("{:?}",result1)
            }
            Err(e) => {
                println!("{:?}", e)
            }
        }
    }
    #[derive(FromJson, Debug)]
    struct User{
        name: String,
        age: f64,
        address: String,
        null_address: Option<String>
    }
}
