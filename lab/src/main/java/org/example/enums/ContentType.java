package org.example.enums;

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
}
