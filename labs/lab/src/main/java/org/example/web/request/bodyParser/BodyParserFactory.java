package org.example.web.request.bodyParser;

import org.example.enums.ContentType;

public class BodyParserFactory {
    private static final JsonParser jsonParser = new JsonParser();
    private static final FormDataParser formDataParser = new FormDataParser();
    private static final FormUrlencodedParser formUrlencodedParser = new FormUrlencodedParser();
    public static BodyParser getBodyParserByContentType(ContentType contentType){
        switch (contentType) {
            case JSON -> {
                return jsonParser;
            }
            case FORM_DATA -> {
                return formDataParser;
            }
            case FORM_URLENCODED -> {
                return formUrlencodedParser;
            }
            default -> throw new IllegalStateException("Unexpected value: " + contentType);
        }
    }
}
