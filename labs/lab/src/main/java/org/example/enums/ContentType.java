package org.example.enums;

import org.example.annotations.Nullable;
import org.example.utils.StringUtils;

public enum ContentType {
    JSON("application/json"),
    FORM_DATA("multipart/form-data"),
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_WEBP("image/webp"),
    IMAGE_SVG("image/svg+xml"),
    PDF("application/pdf"),
    MSWORD("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PLAIN("text/plain"),
    HTML("text/html"),
    CSV("text/csv"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    XML("application/xml"),
    ZIP("application/zip"),
    BINARY("application/octet-stream"),
    MPEG("audio/mpeg"),
    WAV("audio/wav"),
    AUDIO_OGG("audio/ogg"),
    MP3("audio/mp3"),
    MP4("video/mp4"),
    WEBM("video/webm"),
    VIDEO_OGG("video/ogg"),
    ;
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Nullable
    public static ContentType getContentType(String contentType) {
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
