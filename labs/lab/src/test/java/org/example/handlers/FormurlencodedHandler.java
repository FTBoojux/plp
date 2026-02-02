package org.example.handlers;

import org.example.enums.HTTPEnum;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class FormurlencodedHandler implements RequestHandler<Void> {
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        Integer num = httpRequest.getFormData().getInteger("num");
        return "{\"res\":"+(num+1)+"}";
    }

    @Override
    public String getUrl() {
        return "/formUrlencoded";
    }
    @Override
    public String getType(){
        return HTTPEnum.POST.type;
    }
}
