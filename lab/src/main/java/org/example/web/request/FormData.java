package org.example.web.request;

import GlobalEnums.StringEnums;
import org.example.annotations.Nullable;
import org.example.utils.CollectionUtils;
import org.example.utils.StringUtils;
import org.example.utils.json.Bson;
import org.example.utils.json.JsonParser;
import org.example.utils.json.TypeReference;
import org.example.web.multipart.MultipartFile;

import java.util.*;

public class FormData {
    private final Map<String, List<Object>> data = new HashMap<>();
    public static FormData newInstance(){
        return new FormData();
    }
    private FormData(){

    }

    /** 将T类型的值放到key对应的值的尾部
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void put(String key, T value){
        if (this.data.containsKey(key)){
            this.data.get(key).add(value);
        }else{
            List<Object> list = new ArrayList<>();
            list.add(value);
            this.data.put(key,list);
        }
    }
    @Nullable
    private Object getFirstObject(String key){
        List<Object> objectList = this.data.get(key);
        if (CollectionUtils.isEmpty(objectList)) {
            return null;
        }
        return objectList.getFirst();
    }
    /** 获取key对应的第一个值
     * @param key
     * @return
     */
    @Nullable
    private Object getFirst(String key){
        List<Object> objectList = this.data.get(key);
        if (CollectionUtils.isEmpty(objectList)){
            return null;
        }
        Object first = objectList.getFirst();
        if (Objects.isNull(first)){
            return null;
        }
        String string = first.toString();
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(string);
    }

    /**
     * 获取指定key的所有值，如果不存在则返回一个容量为0的列表
     * @param key
     * @return
     */
    public List<Object> getAll(String key){
        List<Object> objectList = this.data.get(key);
        if (CollectionUtils.isEmpty(objectList)) {
            return new ArrayList<>(0);
        }
        return objectList;
    }

    /**
     * 获取指定key的所有值，并尝试将结果转换为指定的类型
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public <T> List<T> getAll(String key, Class<T> clz){
        return this.getAll(key, new TypeReference(clz));
    }
    /**
     * 获取指定key的所有值，并尝试将结果转换为指定的类型
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getAll(String key,TypeReference type){
        List<Object> objectList = this.data.get(key);
        if (CollectionUtils.isEmpty(objectList)) {
            return new ArrayList<>();
        }
        List<T> tList = new ArrayList<>();
        for (Object object : objectList) {
            Object deserialize = Bson.deserialize(object, type);
            tList.add((T)deserialize);
        }
        return tList;
    }


    /**以字符串形式返回指定key的第一个值
     * @param key
     * @return key
     */
    @Nullable
    public String getString(String key){
        List<Object> objectList = this.data.get(key);
        if (CollectionUtils.isEmpty(objectList)){
            return null;
        }
        Object object = objectList.getFirst();
        if (object instanceof String){
            return (String) object;
        }
        throw new ClassCastException("Failed to cast to String for key : " + object);
    }
    @Nullable
    public Integer getInteger(String key){
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)){
            return null;
        }
        return Integer.parseInt(object.toString()) ;
    }
    @Nullable
    public Boolean getBoolean(String key){
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)){
            return null;
        }
        if (StringUtils.equals(object.toString(), StringEnums.TRUE.getString())) {
            return Boolean.TRUE;
        }
        if (StringUtils.equals(object.toString(), StringEnums.FALSE.getString())) {
            return Boolean.FALSE;
        }
        throw new ClassCastException("Failed to cast to Boolean for key : " + object);
    }
    @Nullable
    public Long getLong(String key){
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)) {
            return null;
        }
        return Long.parseLong(object.toString());
    }
    @Nullable
    public Double getDouble(String key) {
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)){
            return null;
        }
        return Double.parseDouble(object.toString());
    }
    @Nullable
    public Float getFloat(String key) {
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)){
            return null;
        }
        return Float.parseFloat(object.toString());
    }
    @Nullable
    public MultipartFile getMultipartFile(String key){
        Object object = this.getFirstObject(key);
        if (Objects.isNull(object)) {
            return null;
        }
        if (object instanceof MultipartFile){
            return (MultipartFile) object;
        }
        throw new ClassCastException("Failed to cast to MultipartFile for key : " + object);
    }

    @Override
    public String toString() {
        return "FormData{" +
                "data=" + data +
                '}';
    }
}
