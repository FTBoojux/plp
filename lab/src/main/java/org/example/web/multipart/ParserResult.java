package org.example.web.multipart;

import java.util.Map;

public class ParserResult {
    enum Status{
        BOUNDARY_FOUND,
        HEADERS_COMPLETE,
        CONTENT_CHUNK,
        ERROR_OVERSIZE,
        ERROR_INVALID_FORMAT,
        FINISHED
    }
    Status status;
    String errorMessage;

}
