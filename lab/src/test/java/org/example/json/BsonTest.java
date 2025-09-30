package org.example.json;

import framework.Bassert;
import framework.Best;
import framework.SimpleTestRunner;
import org.example.json.BsonTestPojo.Example;
import org.example.utils.StringUtils;
import org.example.utils.json.Bson;
import org.example.utils.json.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class BsonTest {
    public static void main(String[] args) {
        BsonTest bsonTest = new BsonTest();
        new SimpleTestRunner().runAllTests(bsonTest);
    }
    @Best
    public void convertToSpecificClass(){
        String json = """
{"username":"Boojux","userId":"b001","detail":{
"goods":[{
"id":"01",
"price":12.3},{
"id":"02",
"price":13.4
}
]
}}
                """;
        User deserialize = Bson.deserializeFromJson(json, User.class);
        System.out.println(deserialize);
    }
    @Best
    public void convertToSpecificClassWithNull(){
        String json = """
{"username":"Boojux","userId":"b001"}
                """;
        User deserialize = Bson.deserializeFromJson(json, User.class);
        System.out.println(deserialize);
    }
    @Best
    public void parseNullToJson(){
        String obj = Bson.serializeToJson(new Object());
        Bassert.fAssert(StringUtils.equals(obj,"{}"),"should get '{}' as result!");
    }
    @Best
    public void parseObjectWithString(){
        Example obj = new Example();
        obj.setStringItem("hello!");
        String json = Bson.serializeToJson(obj);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, new TypeReference(Example.class));
        Bassert.fAssert(obj.equals(objectParsed),"object parsed should equal to source object!");
    }
    @Best
    public void parseObjectWithNumber(){
        Example object = new Example();
        object.setNumberItem(20);
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
    @Best
    public void parseObjectWithBoolean(){
        Example object = new Example();
        object.setBool(true);
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
    @Best
    public void parseObjectWithStringArray(){
        Example object = new Example();
        List<String> list = new ArrayList<>();
        list.add("s1");
        list.add("s2");
        object.setList(list);
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
    @Best
    public void parseObjectWithNullChildObject(){
        Example object = new Example();
        object.setChild(null);
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
    @Best
    public void parseObjectWithNonnullChildObject(){
        Example object = new Example();
        object.setChild(new Example());
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
    @Best
    public void parseObjectWithObjectArray(){
        Example object = new Example();
        List<Example> list = new ArrayList<>();
        Example item = new Example();
        item.setStringItem("1");
        list.add(item);
        item = new Example();
        item.setStringItem("2");
        list.add(item);
        object.setObjList(list);
        String json = Bson.serializeToJson(object);
        System.out.println(json);
        Example objectParsed = Bson.deserializeFromJson(json, Example.class);
        Bassert.fAssert(object.equals(objectParsed),"object with int item should equal to source object");
    }
}
