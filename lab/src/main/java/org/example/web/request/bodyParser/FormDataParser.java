package org.example.web.request.bodyParser;

import org.example.enums.HTTPHeadersEnum;
import org.example.utils.StringUtils;
import org.example.web.RequestHandler;
import org.example.web.multipart.MultipartFile;
import org.example.web.request.FormData;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.Pair;
import org.example.web.utils.io.BoundarySplitter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * 用于处理 multipart/form-data 格式
 */
public class FormDataParser implements BodyParser {
    private static final String HEAD_PREFIX = "Content-Disposition: form-data; name=\"";
    private static final String NAME_PREFIX = "name=\"";
    private static final byte[] NEWLINE_CHARACTERS = {'\r', '\n'};
    private static final byte[] SEMICOLON = {';',' '};
    private static final String FILENAME_PREFIX = "filename=\"";
    private static final String CONTENT_TYPE_PREFIX = "Content-Type: ";
//    @Override
//    public Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, MatchResult matchResult) {
//        FormData formData = FormData.newInstance();
//
//        String content_type = headers.getOrDefault(HTTPHeadersEnum.CONTENT_TYPE.getHeader(), StringEnums.EMPTY.getString());
//        String[] lines = splitByBoundary(rawBody, content_type);
//        for (String line : lines) {
//            line = line.trim();
//            String[] content = line.split("\n");
//            if (content.length < 3){
//                continue;
//            }
//            String content_disposition = content[0].trim();
//            String name = content_disposition.substring(prefix.length(), content_disposition.length()-1).trim();
//            String value = content[content.length-2].trim();
//            if (StringUtils.isEmpty(value)) {
//                formData.put(name, StringEnums.EMPTY.getString());
//            }else {
//                formData.put(name, value);
//            }
//        }
//        httpRequest.setFormData(formData);
//    }

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
        String boundary = origin_boundary.substring(index + 1);
        return rawBody.split(boundary);
    }

//    public Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, RequestHandler requestHandler) throws IOException {
//        PushbackInputStream pis = new PushbackInputStream(inputStream);
//        String content_type = headers.get(HTTPHeadersEnum.CONTENT_TYPE.getHeader());
//        String[] content_types = content_type.split(";");
//        if (content_types.length < 2){
//            throw new IllegalArgumentException("Content-Type of http request must have type and boundary : " + content_type);
//        }
//        String origin_boundary = content_types[1];
//        int index = origin_boundary.indexOf('=');
//        if (index < 0) {
//            throw new UnsupportedOperationException("Incorrect boundary : " + origin_boundary);
//        }
//        String boundary = origin_boundary.substring(index+1);
//        byte[] buffer = new byte[8096];
//        String _contentLength = headers.get(HTTPHeadersEnum.CONTENT_LENGTH.getHeader());
//        if (StringUtils.isEmpty(_contentLength)) {
//            throw new IllegalArgumentException("Lack Content-Length.");
//        }
//        long contentLength = Long.parseLong(_contentLength);
//        long read = 0;
//        while (read < contentLength){
//            read += pis.read(buffer);
//        }
//
//    }

    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, RequestHandler requestHandler) throws IOException {
        int contentLength = headers.getContentLength();
        byte[] bytesRead = inputStream.readNBytes(contentLength);
        String contentType = headers.getByEnum(HTTPHeadersEnum.CONTENT_TYPE);
        FormData formData = FormData.newInstance();
        if (!StringUtils.isEmpty(contentType)) {
            String boundary = contentType.substring(contentType.indexOf('=') + 1);
            List<byte[]> pairs = BoundarySplitter.splitByteArrayByBoundary(bytesRead, boundary.getBytes(StandardCharsets.UTF_8));
            for (byte[] pair : pairs) {
                List<byte[]> lines = BoundarySplitter.splitByteArrayByBoundary(pair, NEWLINE_CHARACTERS);
                if (lines.size() < 3) {
                    continue;
                }
                byte[] headInformation = lines.get(1);
                List<byte[]> headerInformationList = BoundarySplitter.splitByteArrayByBoundary(headInformation, SEMICOLON);
                if (headerInformationList.size() < 2) {
                    throw new IllegalArgumentException("Illegal format of form-data!");
                }
                String name = new String(Arrays.copyOfRange(headerInformationList.get(1),
                        NAME_PREFIX.length(),
                        headerInformationList.get(1).length - 1)
                );
                if (headerInformationList.size() == 2) {
                    String value = new String(lines.get(3));
                    formData.put(name, value);
                } else if (headerInformationList.size() == 3) {
                    MultipartFile multipartFile = new MultipartFile();
                    String filename = new String(Arrays.copyOfRange(headerInformationList.getLast(),
                            FILENAME_PREFIX.length(),
                            headerInformationList.getLast().length-1));
                    multipartFile.setFilename(filename);
                    byte[] contentTypeBytes = lines.get(2);
                    String contentTypeOfFile = new String(Arrays.copyOfRange(contentTypeBytes,
                            CONTENT_TYPE_PREFIX.length(),
                            contentTypeBytes.length));
                    multipartFile.setContentType(contentTypeOfFile);
                    multipartFile.setBytes(lines.get(4));
                    formData.put(name, multipartFile);
                }
            }
        }
        return new Pair<>(formData, new Object());
    }
}
