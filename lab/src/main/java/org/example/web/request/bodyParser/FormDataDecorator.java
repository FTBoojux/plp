package org.example.web.request.bodyParser;

import GlobalEnums.StringEnums;
import org.example.enums.HTTPHeaders;
import org.example.utils.StringUtils;
import org.example.web.request.FormData;
import org.example.web.request.HttpRequest;
import org.example.web.utils.web.MatchResult;

public class FormDataDecorator implements BodyParser{
    private final String prefix = "Content-Disposition: form-data; name=\"";
    @Override
    public void extractBodyData(String rawBody, HttpRequest<Object> httpRequest, MatchResult matchResult) {
        FormData formData = new FormData();

        String content_type = httpRequest.getHeaders().getOrDefault(HTTPHeaders.CONTENT_TYPE.getHeader(), StringEnums.EMPTY.getString());
        String[] lines = splitByBoundary(rawBody, content_type);
        for (String line : lines) {
            String[] content = line.split("\n");
            if (content.length < 3){
                continue;
            }
            String content_disposition = content[0];
            String name = content_disposition.substring(prefix.length() + 1,content_disposition.length()-1);
            String value = content[content.length-1];
            if (StringUtils.isEmpty(value)) {
                formData.put(name, StringEnums.EMPTY.getString());
            }else {
                formData.put(name, value);
            }
        }
        httpRequest.setFormData(formData);
    }

    private static String[] splitByBoundary(String rawBody, String content_type) {
        String[] content_types = content_type.split(";");
        if (content_types.length < 2) {
            throw new ArrayIndexOutOfBoundsException("Content-Type of http request must have more type and boundary : " + content_type);
        }
        String origin_boundary = content_types[1];
        int index = origin_boundary.indexOf('=');
        if (index < 0) {
            throw new UnsupportedOperationException("Incorrect boundary : " + origin_boundary);
        }
        String boundary = origin_boundary.substring(index+1);
        return rawBody.split(boundary);
    }
}
