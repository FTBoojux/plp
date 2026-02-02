package org.example.web.handlers;

import org.example.enums.HTTPEnum;
import org.example.utils.StringUtils;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class IdentityCheckHandler implements RequestHandler<HashMap<String, String>> {
    Map<String, String> mockDatabase = new HashMap<>();
    {
        mockDatabase.put("administrator","13157");
        mockDatabase.put("Boojux","oYeah");
        mockDatabase.put("workerA","fdaw32");
    }
    @Override
    public Object doHandle(HttpRequest<HashMap<String, String>> httpRequest) {
        if (StringUtils.equals(httpRequest.getBody().get("password"),
                mockDatabase.get(httpRequest.getBody().get("username")))){
            return "passed!";
        }
        return "failed!";
    }

    @Override
    public String getUrl() {
        return "/check";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
