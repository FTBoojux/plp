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
    private static final String NAME_PREFIX = "name=\"";
    private static final byte[] NEWLINE_CHARACTERS = {'\r', '\n'};
    private static final byte[] SEMICOLON = {';',' '};
    private static final String FILENAME_PREFIX = "filename=\"";
    private static final String CONTENT_TYPE_PREFIX = "Content-Type: ";

    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, RequestHandler<?> requestHandler) throws IOException {
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
