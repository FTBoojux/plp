package org.example.json;

import framework.FTest;
import framework.SimpleTestRunner;
import org.example.utils.json.Bson;

public class BsonTest {
    public static void main(String[] args) {
        BsonTest bsonTest = new BsonTest();
        new SimpleTestRunner().runAllTests(bsonTest);
    }
    @FTest
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
        User deserialize = Bson.deserialize(json, User.class);
        System.out.println(deserialize);
    }
    @FTest
    public void convertToSpecificClassWithNull(){
        String json = """
{"username":"Boojux","userId":"b001"}
                """;
        User deserialize = Bson.deserialize(json, User.class);
        System.out.println(deserialize);
    }
}
