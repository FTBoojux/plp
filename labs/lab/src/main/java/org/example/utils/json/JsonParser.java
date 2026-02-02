package org.example.utils.json;

import org.example.web.exceptions.InvalidRequestBodyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private String json;
    private int index;
    public Object parse(String json){
        this.json = json;
        this.index = 0;
        skipWhitespace();
        Object obj = parseValue();
        skipWhitespace();
        if(index < json.length()){
            throw new InvalidRequestBodyException("Unexpected character after JSON at index: " + this.index);
        }
        return obj;
    }

    private Object parseValue() {
        skipWhitespace();
        char c = this.json.charAt(this.index);
        return switch (c) {
            case '{' -> parseObject();
            case '[' -> parseArray();
            case '"' -> parseString();
            case 't', 'f' -> parseBoolean();
            case 'n' -> parseNull();
            default -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield parseNumber();
                }
                throw new InvalidRequestBodyException("Unknown character: " + c);
            }
        };
    }

    private Number parseNumber() {
//        boolean negative = this.peek() == '-';
        int start = this.index;
        if(this.peek() == '-'){
            ++this.index;
        }
        while (Character.isDigit(this.peek())){
            ++this.index;
        }
        boolean isDecimal = this.peek() == '.';
        if (isDecimal){
            ++this.index;
        }
        while (Character.isDigit(this.peek())){
            ++this.index;
        }
        // 科学计数法
        if (this.peek() == 'e' || this.peek() == 'E'){
            isDecimal = true;
            ++this.index;
            if(this.peek() == '+' || this.peek() == '-'){
                ++this.index;
            }
            if(!Character.isDigit(this.peek())){
                throw new InvalidRequestBodyException("Invalid numberStr at index: " + this.index);
            }
            while (Character.isDigit(this.peek())){
                ++this.index;
            }

        }
        String numberStr = this.json.substring(start, this.index);
        if (isDecimal){
            return Double.parseDouble(numberStr);
        }else{
            long number = Long.parseLong(numberStr);
            if(number <= Integer.MAX_VALUE && number >= Integer.MIN_VALUE){
                return (int)number;
            }else{
                return number;
            }
        }
    }

    private Object parseNull() {
        if (this.json.startsWith("null", this.index)) {
            this.index += 4;
            return null;
        }
        throw new InvalidRequestBodyException("Invalid token at index: " + this.index);
    }

    private boolean parseBoolean() {
        if (this.json.startsWith("true",this.index)){
            this.index += 4;
            return true;
        } else if (this.json.startsWith("false", this.index)) {
            this.index += 5;
            return false;
        }
        throw new InvalidRequestBodyException("Invalid token at index: " + this.index);
    }

    private List<Object> parseArray() {
        List<Object> array = new ArrayList<>();
        consume('[');
        skipWhitespace();

        if (peek() == ']'){
            consume(']');
            return array;
        }
        while (true){
            array.add(parseValue());
            skipWhitespace();

            if(peek() == ','){
                consume(',');
                skipWhitespace();
            }else if(peek() == ']'){
                consume(']');
                skipWhitespace();
                break;
            }else{
                throw new InvalidRequestBodyException("Expected ',' or ']' at index: " + this.index);
            }
        }
        return array;
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> result = new HashMap<>();
        consume('{');
        skipWhitespace();
        if(peek() == '}'){
            consume('}');
            return result;
        }
        while (true){
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            consume(':');
            skipWhitespace();
            Object value = parseValue();
            result.put(key, value);
            skipWhitespace();
            if (peek() == ','){
                consume(',');
                continue;
            }else if(peek() == '}'){
                consume('}');
                break;
            }else{
                throw new InvalidRequestBodyException("Expect ',' or '}' at position: " + this.index);
            }
        }
        return result;
    }

    private String parseString() {
        consume('"');
        StringBuilder sb = new StringBuilder();
        while (this.index < this.json.length() && this.json.charAt(this.index) != '"'){
            char c = this.json.charAt(this.index);
            if(c == '\\'){
                ++this.index;
                if(this.index > this.json.length()){
                    throw new InvalidRequestBodyException("Unexpected end in string at index: " + this.index);
                }
                char next = this.json.charAt(this.index);
                switch (next){
                    case '"':
                    case '\\':
                    case '/':
                        sb.append(next);
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'u':
                        // unicode : /uXXXX
                        if (this.index + 4 >= this.json.length()){
                            throw new InvalidRequestBodyException("Invalid unicode at index: " + this.index);
                        }
                        String unicode = this.json.substring(this.index+1, this.index+5);
                        sb.append((char)Integer.parseInt(unicode,16));
                        this.index+=4;
                        break;
                }
            }else{
                sb.append(c);
            }
            ++this.index;
        }
        consume('"');
        return sb.toString();
    }

    private void consume(char c) {
        ++this.index;
    }
    private char peek(){
        return this.index < json.length() ? json.charAt(index) : '\0';
    }

    private void skipWhitespace(){
        while(this.index < this.json.length() && Character.isWhitespace(this.json.charAt(this.index))){
            ++this.index;
        }
    }
}
