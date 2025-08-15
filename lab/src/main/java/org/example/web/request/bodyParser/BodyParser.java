package org.example.web.request.bodyParser;

import org.example.web.request.HttpRequest;
import org.example.web.utils.web.MatchResult;

/**
 * 用于将request body根据不同的Content-Type加工成需要的形式并填充到httpRequest中
 */
public interface BodyParser {
    void extractBodyData(String rawBody, HttpRequest<Object> httpRequest, MatchResult matchResult);
}
