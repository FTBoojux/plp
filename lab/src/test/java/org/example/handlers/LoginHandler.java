package org.example.handlers;

import org.example.enums.HTTPEnum;
import org.example.utils.StringUtils;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements RequestHandler<Login> {
    Map<String, String> mockDatabase = new HashMap<>();
    {
        mockDatabase.put("administrator","13157");
        mockDatabase.put("Boojux","oYeah");
        mockDatabase.put("workerA","fdaw3@2");
        mockDatabase.put("张玉辉","dadwa");
    }
    @Override
    public Object doHandle(HttpRequest<Login> httpRequest) {
        Login body = httpRequest.getBody();
        if (StringUtils.equals(body.getPassword(), mockDatabase.get(body.getUsername()))){
            return "succeed!";
        }else{
            return "fail!";
        }
    }

    @Override
    public String getUrl() {
        return "/login";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
