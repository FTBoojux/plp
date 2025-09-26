package org.example.interceptors;

import org.example.utils.StringUtils;
import org.example.web.middleware.MiddlewareHandler;
import org.example.web.request.HttpRequest;
import org.example.web.utils.http.HttpResponseBuilder;

public class AuthInterceptor implements MiddlewareHandler {
    @Override
    public boolean handle(HttpRequest<?> httpRequest, HttpResponseBuilder response) {
        String path = httpRequest.getPath();
        if (path.startsWith("/auth/")){
            String user = httpRequest.getHeaders().get("user");
            // 检查请求头中有无用户名
            // check whether user exist in headers or not
            if (StringUtils.isEmpty(user)) {
                response.statusCode(403).reasonPhrase("Forbidden");
                return false;
            }
        }
        // 放行 pass
        return true;
    }
}
