package org.example.web.request.bodyParser;

import GlobalEnums.StringEnums;
import org.example.enums.HTTPHeadersEnum;
import org.example.utils.StringUtils;
import org.example.web.RequestHandler;
import org.example.web.request.FormData;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.Pair;
import org.example.web.utils.web.MatchResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Map;


/**
 * 用于处理 multipart/form-data 格式
 */
public class FormDataParser implements BodyParser{
    private final String prefix = "Content-Disposition: form-data; name=\"";
    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, MatchResult matchResult) {
        FormData formData = FormData.newInstance();

        String content_type = headers.getOrDefault(HTTPHeadersEnum.CONTENT_TYPE.getHeader(), StringEnums.EMPTY.getString());
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

    public Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, RequestHandler requestHandler) throws IOException {
        PushbackInputStream pis = new PushbackInputStream(inputStream);
        String content_type = headers.get(HTTPHeadersEnum.CONTENT_TYPE.getHeader());
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
        String _contentLength = headers.get(HTTPHeadersEnum.CONTENT_LENGTH.getHeader());
        if (StringUtils.isEmpty(_contentLength)) {
            throw new IllegalArgumentException("Lack Content-Length.");
        }
        long contentLength = Long.parseLong(_contentLength);
        long read = 0;
        while (read < contentLength){
            read += pis.read(buffer);
        }

    }

    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, RequestHandler requestHandler) throws IOException {
        int contentLength = headers.getContentLength();
        byte[] bytesRead = inputStream.readNBytes(contentLength);
        String boundary = headers.getByEnum(HTTPHeadersEnum.BOUNDARY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(contentLength);

    }
}
