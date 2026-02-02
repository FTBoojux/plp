package org.example.web.request.bodyParser;

import org.example.web.RequestHandler;
import org.example.web.request.FormData;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.Pair;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于处理 application/x-www-form-urlencoded 格式的数据
 */
public class FormUrlencodedParser implements BodyParser{
    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, RequestHandler<?> requestHandler) throws IOException {
        byte[] bytes = inputStream.readNBytes(headers.getContentLength());
        String rawBody = new String(bytes);
        FormData formData = FormData.newInstance();
        String[] pairs = rawBody.split("&");
        for (String pair : pairs) {
            int index = pair.indexOf('=');
            String name = pair.substring(0,index);
            String value = pair.substring(index+1);
            formData.put(name, value);
        }
        return new Pair<>(formData, new Object());
    }
}
