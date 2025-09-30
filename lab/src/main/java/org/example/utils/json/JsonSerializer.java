package org.example.utils.json;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonSerializer {
    StringBuilder sb = new StringBuilder();
    public JsonSerializer(Object object) {
        addObject(object);
    }

    private void addObject(Object object) {
        sb.append("{");
        Class<?> clz = object.getClass();
        while (clz != null) {
            Iterator<Field> fieldIterator = Arrays.asList(clz.getDeclaredFields()).iterator();
            while (fieldIterator.hasNext()){
                Field field = fieldIterator.next();
                field.setAccessible(true);
                try {
                    String key = field.getName();
                    Object value = field.get(object);
                    sb.append(String.format("\"%s\":",key));
                    append(value);
                    if (fieldIterator.hasNext()) {
                        sb.append(",");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            clz = clz.getSuperclass();
        }
        sb.append("}");
    }
    private void append(Object obj){
        switch (obj) {
            case null -> sb.append("null");
            case String s -> sb.append(String.format("\"%s\"", obj));
            case Number number -> sb.append(String.format("%s", obj));
            case Boolean bool -> sb.append(String.format("%s",obj));
            case List<?> list -> appendList(list);
            case Map<?,?> map -> appendMap(map);
            case Object object -> addObject(obj);
        }
    }

    private void appendMap(Map<?, ?> map) {
        sb.append("{");
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<?, ?> entry = iterator.next();
            sb.append(String.format("\"%s\":",entry.getKey()));
            append(entry.getValue());
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("}");
    }

    private void appendList(List<?> list) {
        sb.append("[");
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            append(item);
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("]");
    }

    public String get(){
        return sb.toString();
    }
}
