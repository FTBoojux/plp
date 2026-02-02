package org.example.web.handlers;

import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;

public class QuestionParamHandler implements RequestHandler<HashMap<String, String>> {
    @Override
    public Object doHandle(HttpRequest<HashMap<String, String>> httpRequest) {
        HashMap<String, String> params = httpRequest.getParams();
        return "receive params: " + params.toString();
    }

    @Override
    public String getUrl() {
        return "/pageQuery";
    }
}
