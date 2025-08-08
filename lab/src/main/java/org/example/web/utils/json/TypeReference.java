package org.example.web.utils.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeReference {
    private final Type type;

    public Type getType() {
        return type;
    }
    public TypeReference(Type type){
        this.type = type;
    }
    protected TypeReference(){
        Type superClass = this.getClass().getGenericSuperclass();
        this.type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
    }
}
