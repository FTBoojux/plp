package org.example.json;

import framework.Bassert;
import framework.Best;
import framework.SimpleTestRunner;
import org.example.utils.StringUtils;
import org.example.utils.json.Bson;

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
}
