package org.example.json.BsonTestPojo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Example {
    public Example(){

    }

    public String getStringItem() {
        return stringItem;
    }

    public void setStringItem(String stringItem) {
        this.stringItem = stringItem;
    }

    private String stringItem;

    public Integer getNumberItem() {
        return numberItem;
    }

    public void setNumberItem(int numberItem) {
        this.numberItem = numberItem;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    private Boolean bool;

    private Integer numberItem;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setNumberItem(Integer numberItem) {
        this.numberItem = numberItem;
    }

    private List<String> list;

    public List<Example> getObjList() {
        return objList;
    }

    public void setObjList(List<Example> objList) {
        this.objList = objList;
    }

    private List<Example> objList;

    public Example getChild() {
        return child;
    }

    public void setChild(Example child) {
        this.child = child;
    }

    private Example child;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Example example = (Example) object;
        return Objects.equals(stringItem, example.stringItem) && Objects.equals(bool, example.bool) && Objects.equals(numberItem, example.numberItem) && Objects.equals(list, example.list) && Objects.equals(objList, example.objList) && Objects.equals(child, example.child) && Objects.equals(map, example.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringItem, bool, numberItem, list, objList, child, map);
    }

    private Map<String, String> map;
}
