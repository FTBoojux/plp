package org.example.web.utils.json;

import org.example.web.exceptions.InvalidRequestBodyException;

import java.lang.reflect.*;
import java.util.*;

public class Bson {
    public static <T> T deserialize(String json, Class<T> clz){
        JsonParser jp = getJsonParser();
        Object obj = jp.parse(json);
        return (T) deserialize(obj, new TypeReference(clz));
    }

    //    private Object convertToType(Object obj, Type)
    public static <T> T deserialize(String json, TypeReference typeReference) {
        JsonParser jp = getJsonParser();
        Object obj = jp.parse(json);
        return (T) deserialize(obj, typeReference);
    }
    public static Object deserialize(Object obj, TypeReference typeReference) {
        Type type = typeReference.getType();
        Class<?> clz = null;
        if (type instanceof ParameterizedType){
            clz = (Class<?>) ((ParameterizedType)type).getRawType();
        }else if(type instanceof Class){
            clz = (Class<?>) type;
        }
        if (clz == String.class) {
            return obj.toString();
        } else if (clz == Integer.class || clz == int.class) {
            return ((Number) obj).intValue();
        } else if (clz == Long.class || clz == long.class) {
            return ((Number) obj).longValue();
        } else if (clz == Double.class || clz == double.class) {
            return ((Number) obj).doubleValue();
        } else if (clz == Float.class || clz == float.class) {
            return ((Number) obj).floatValue();
        } else if (clz == Boolean.class || clz == boolean.class) {
            return obj;
        } else if (clz == List.class) {
            return convertToList(obj, type);
        } else if (clz == Map.class) {
            return convertToMap(obj, type);
        } else if (clz.isArray()) {
            return convertToArray(obj, clz.getComponentType());
        }else {
            return convertToObject(obj, clz);
        }
    }

    private static List<?> convertToList(Object obj, Type type) {
        if (!(obj instanceof List)) {
            throw new InvalidRequestBodyException("Expect list but get: " + obj);
        }
        List<Object> objList = (List<Object>) obj;
        Type elementType = Object.class;
        if (type instanceof ParameterizedType){
            Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArray.length > 0){
                elementType = typeArray[0];
            }
        }
        List<Object> result = new ArrayList<>();
        for (Object object : objList) {
            result.add(deserialize(object, new TypeReference(elementType)));
        }
        return result;
    }

    private static Map<?,?> convertToMap(Object obj, Type type){
        if (!(obj instanceof Map)){
            throw new InvalidRequestBodyException("Expect map but get: " + obj);
        }
        Map<String, Object> objMap = (Map<String, Object>) obj;
        Type keyType = String.class;
        Type valueType = Object.class;
        if (type instanceof ParameterizedType){
            Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
            if (typeArray.length > 0){
                keyType = typeArray[0];
            }
            if (typeArray.length > 1){
                valueType = typeArray[1];
            }
        }
        Map<Object, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : objMap.entrySet()) {
            Object key = deserialize(entry.getKey(), new TypeReference(keyType));
            Object value = deserialize(entry.getValue(), new TypeReference(valueType));
            result.put(key, value);
        }
        return result;
    }
    private static Object convertToArray(Object obj, Class<?> clz){
        if (!(obj instanceof List)){
            throw new InvalidRequestBodyException("Expect array but get: " + obj);
        }
        List<Object> list = (List<Object>) obj;
        Object array = Array.newInstance(clz, list.size());
        for (int i = 0; i < list.size(); i++) {
            Array.set(array, i, deserialize(list.get(i), new TypeReference(clz)));
        }
        return array;
    }
    private static Object convertToObject(Object obj, Class<?> clz){
        if (!(obj instanceof Map)){
            throw new InvalidRequestBodyException("Expect object but get: " + obj);
        }
        Map<String, Object> objMap = (Map<String, Object>) obj;
        try {
            // 1 try no-args constructor
            return createByNoArgsConstructor(clz, objMap);
        } catch (Exception e) {
//            throw new InvalidRequestBodyException("Failed to create instance of " + clz.getName() +" ,\n message: " + e.getMessage());
            try{
                // 2 try constructor by matching fields
                return createByConstructor(clz, objMap);
            } catch (Exception ex) {
                throw new InvalidRequestBodyException("cannot create instance of "+ clz.getName());
            }
        }
    }

    private static Object createByConstructor(Class<?> clz, Map<String, Object> objMap) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Parameter[] parameters = clz.getDeclaredConstructors()[0].getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameter.getName();
            Object object = objMap.get(parameterName);
            args[i] = convertToObject(object, parameter.getType());
        }
        return clz.getDeclaredConstructors()[0].newInstance(args);
    }

    private static Object createByNoArgsConstructor(Class<?> clz, Map<String, Object> objMap) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> declaredConstructor = clz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object instance = declaredConstructor.newInstance();
        List<Field> fields = getAllFields(clz);

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (objMap.containsKey(name)) {
                Object valueObj = objMap.get(name);
                Type genericType = field.getGenericType();
                Object value = deserialize(valueObj, new TypeReference(genericType));
                field.set(instance, value);
            }
        }

        return instance;
    }

    private static List<Field> getAllFields(Class<?> clz) {
        List<Field> fields = new ArrayList<>();
        while (clz != null && clz != Object.class){
            fields.addAll(Arrays.asList(clz.getDeclaredFields()));
            clz = clz.getSuperclass();
        }
        return fields;
    }

    private static JsonParser getJsonParser() {
        return new JsonParser();
    }
}
