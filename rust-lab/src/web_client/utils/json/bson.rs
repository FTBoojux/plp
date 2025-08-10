use crate::web_client::error::err::Berror::InvalidRequestBodyError;
use std::collections::HashMap;
use std::str::Chars;

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
impl JsonParser {
    pub fn new(json_str: &str) -> Self {
        let mut chars = json_str.chars();
        let top = chars.next();
        JsonParser {
            json_it: chars,
            index: 0,
            peek: top,
        }
    }
    pub fn parse(&mut self) -> Result<Option<JsonType>, Err> {
        self.skip_whitespace();
        self.parse_value()
    }

    fn parse_value(&mut self) -> Result<Option<JsonType>, Err> {
        self.skip_whitespace();
        match self.peek.unwrap() {
            '{' => self.parse_value(),
            '[' => self.parse_array(),
            '"' => self.parse_string(),
            't' => self.parse_boolean(),
            'f' => self.parse_boolean(),
            'n' => self.parse_null(),
            _ => {
                if self.peek.unwrap().is_digit(10) || self.peek.unwrap() == '-' {
                    return self.parse_number();
                }
                Err(InvalidRequestBodyError(format!(
                    "Unknow character : {}",
                    self.peek.unwrap_or("")
                )))
            }
        }
    }
    fn parse_number(&mut self) -> Result<JsonType::Number, InvalidRequestBodyError> {
        let mut string = String::new();
        if self.peek() == '-' {
            self.pop();
            string.push('-');
        }
        while self.peek().is_digit(10) {
            string.push(self.pop());
        }
        let mut is_demical = self.peek() == '.';
        if is_demical { self.pop(); }
        while self.peek().is_digit(10) {
            string.push(self.pop());
        }
        if self.peek() == 'e' || self.peek() == 'E' {
            is_demical = true;
            self.pop();
            if self.peek() == '+' || self.peek() == '-' {
                string.push(self.pop())
            }
            if !self.peek().is_digit(10) {
                return Err(InvalidRequestBodyError(format!("Invalid number at index: {}", self.index)));
            }
            while self.peek().is_digit(10) {
                string.push(self.pop());
            }
        }
        let number = string.parse().unwrap();
        Ok(JsonType::Number(number))
    }

    fn pop(&mut self) -> char {
        self.peek = self.json_it.next();
        self.peek.unwrap()
    }

    pub fn skip_whitespace(&mut self) {
        while self.peek.is_some() && self.peek.unwrap().is_whitespace() {
            self.peek = self.json_it.next();
            self.index += 1;
        }
    }
    pub fn peek(&self) -> char {
        self.peek.unwrap()
    }
}
