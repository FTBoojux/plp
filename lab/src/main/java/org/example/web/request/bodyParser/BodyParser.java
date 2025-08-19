package org.example.web.request.bodyParser;

import org.example.web.request.FormData;
import org.example.web.request.HttpRequest;
import org.example.web.utils.Pair;
import org.example.web.utils.web.MatchResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 用于将request body根据不同的Content-Type加工成需要的形式并填充到httpRequest中
 */
public interface BodyParser {
    Pair<FormData, Object> extractBodyData(InputStream inputStream, Map<String, String> headers, MatchResult matchResult) throws IOException;
}
