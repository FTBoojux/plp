package org.example.handlers;

import org.example.enums.HTTPEnum;
import org.example.web.RequestHandler;
import org.example.web.request.FormData;
import org.example.web.request.HttpRequest;

import java.util.HashSet;
import java.util.Set;

public class FormDataHandler implements RequestHandler<Void> {
    Set<String> usernames = new HashSet<>();
    {
        usernames.add("admin");
        usernames.add("Boojux");
    }
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        FormData formData = httpRequest.getFormData();
        String username = formData.getString("username");
        if (usernames.contains(username)){
            return "{\"msg\":\"this username had been used!\"}";
        }
        return "{\"msg\":\"this username is valid!\"}";
    }

    @Override
    public String getUrl() {
        return "/formData";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
