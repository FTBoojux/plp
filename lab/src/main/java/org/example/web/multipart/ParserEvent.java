package org.example.web.multipart;

import java.util.Map;

public class ParserEvent {
    enum EventType{
        HEADERS_FOUND,
        CONTENT_DATA,
        CHUNK_FINISHED
    }
    EventType eventType;
    String partName;
    byte[] data;
    Map<String, String> headers;
}
