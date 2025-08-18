package org.example.web.request.bodyParser;

import org.example.web.request.FormData;
import org.example.web.request.HttpRequest;
import org.example.web.utils.web.MatchResult;

/**
 * 用于处理 application/x-www-form-urlencoded 格式的数据
 */
public class FormUrlencodedParser implements BodyParser{
    @Override
    public void extractBodyData(String rawBody, HttpRequest<Object> httpRequest, MatchResult matchResult) {
        FormData formData = FormData.newInstance();
        String[] pairs = rawBody.split("&");
        for (String pair : pairs) {
            int index = pair.indexOf('=');
            String name = pair.substring(0,index);
            String value = pair.substring(index+1);
            formData.put(name, value);
        }
        httpRequest.setFormData(formData);
    }
}
