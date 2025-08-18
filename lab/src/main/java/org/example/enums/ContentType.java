package org.example.enums;

import org.example.annotations.Nullable;
import org.example.utils.StringUtils;

public enum ContentType {
    JSON("application/json"),
    FORM_DATA("multipart/form-data"),
    FORM_URLENCODED("application/x-www-form-urlencoded");
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    @Nullable
    public static ContentType getContentType(String contentType){
        if (StringUtils.isEmpty(contentType)) {
            return null;
        }
        for (ContentType value : ContentType.values()) {
            if (contentType.startsWith(value.type)) {
                return value;
            }
        }
        return null;
    }
}
