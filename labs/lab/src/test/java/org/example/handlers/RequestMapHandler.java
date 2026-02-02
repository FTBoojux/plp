package org.example.handlers;

import org.example.enums.HTTPEnum;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

public class RequestMapHandler implements RequestHandler<RequestMapHandler.Param> {
    @Override
    public Object doHandle(HttpRequest<Param> httpRequest) {
//        String body = httpRequest.getBody();
//        Param deserialize = Bson.deserialize(body, Param.class);
        return null;
    }

    @Override
    public String getUrl() {
        return "/testPostRequest";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }

    public static class Param{
        private String param1;
        private String p;

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
}
