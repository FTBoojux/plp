package org.example.web.request.bodyParser;

import org.example.annotations.Optional;
import org.example.web.RequestHandler;
import org.example.web.request.FormData;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 用于将request body根据不同的Content-Type加工成需要的形式并填充到httpRequest中
 */
public interface BodyParser {
    Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, @Optional RequestHandler requestHandler) throws IOException;
}
