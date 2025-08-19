package org.example.web.request.bodyParser;

import GlobalEnums.StringEnums;
import org.example.enums.HTTPHeaders;
import org.example.utils.StringUtils;
import org.example.web.request.FormData;
import org.example.web.request.HttpRequest;
import org.example.web.utils.Pair;
import org.example.web.utils.web.MatchResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Map;


/**
 * 用于处理 multipart/form-data 格式
 */
public class FormDataParser implements BodyParser{
    private final String prefix = "Content-Disposition: form-data; name=\"";
    public void extractBodyData(String rawBody, HttpRequest<Object> httpRequest, MatchResult matchResult) {
        FormData formData = FormData.newInstance();

        String content_type = httpRequest.getHeaders().getOrDefault(HTTPHeaders.CONTENT_TYPE.getHeader(), StringEnums.EMPTY.getString());
        String[] lines = splitByBoundary(rawBody, content_type);
        for (String line : lines) {
            line = line.trim();
            String[] content = line.split("\n");
            if (content.length < 3){
                continue;
            }
            String content_disposition = content[0].trim();
            String name = content_disposition.substring(prefix.length(), content_disposition.length()-1).trim();
            String value = content[content.length-2].trim();
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

    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, MatchResult matchResult) throws IOException {
        PushbackInputStream pis = new PushbackInputStream(inputStream);
        String content_type = headers.get(HTTPHeaders.CONTENT_TYPE.getHeader());
        String[] content_types = content_type.split(";");
        if (content_types.length < 2){
            throw new IllegalArgumentException("Content-Type of http request must have type and boundary : " + content_type);
        }
        String origin_boundary = content_types[1];
        int index = origin_boundary.indexOf('=');
        if (index < 0) {
            throw new UnsupportedOperationException("Incorrect boundary : " + origin_boundary);
        }
        String boundary = origin_boundary.substring(index+1);
        byte[] buffer = new byte[8096];
        String _contentLength = headers.get(HTTPHeaders.CONTENT_LENGTH.getHeader());
        if (StringUtils.isEmpty(_contentLength)) {
            throw new IllegalArgumentException("Lack Content-Length.");
        }
        long contentLength = Long.parseLong(_contentLength);
        long read = 0;
        while (read < contentLength){
            read += pis.read(buffer);
        }

    }
}
