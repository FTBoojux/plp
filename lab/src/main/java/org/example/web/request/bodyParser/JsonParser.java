package org.example.web.request.bodyParser;

import org.example.annotations.Optional;
import org.example.utils.StringUtils;
import org.example.utils.json.Bson;
import org.example.utils.json.TypeReference;
import org.example.web.RequestHandler;
import org.example.web.request.FormData;
import org.example.web.request.HttpHeaders;
import org.example.web.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理 application/json 格式的数据
 */
public class JsonParser implements BodyParser {
    private static final Map<RequestHandler<?>, TypeReference> requestBodyClzMap = new HashMap<>();

    private static TypeReference getRequestClass(RequestHandler requestHandler) {
//        Class<?> requestClz = Void.class;
        TypeReference typeReference = new TypeReference(Void.class);
        Type[] requestInterfaces = requestHandler.getClass().getGenericInterfaces();
        for (Type requestInterface : requestInterfaces) {
            if (requestInterface instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) requestInterface;
                if (parameterizedType.getRawType() == RequestHandler.class){
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if(actualTypeArguments.length > 0){
//                        requestClz = (Class<?>) actualTypeArguments[0];
                        typeReference = new TypeReference(actualTypeArguments[0]);
                    }
                }
            }
        }
        return typeReference;
    }

    @Override
    public Pair<FormData, Object> extractBodyData(InputStream inputStream, HttpHeaders headers, @Optional RequestHandler requestHandler) throws IOException {
        TypeReference requestClass = getRequestClass(requestHandler);
        if (!requestBodyClzMap.containsKey(requestHandler)){
            requestBodyClzMap.put(requestHandler, getRequestClass(requestHandler));
        }
        TypeReference typeReference = requestBodyClzMap.get(requestHandler);
        String rawBody = new String(inputStream.readNBytes(contentLength));
        if (Void.class != typeReference.getType() && !StringUtils.isEmpty(rawBody)){
            Object requestBody = Bson.deserializeFromJson(rawBody, typeReference);
            return new Pair<>(FormData.newInstance(), requestBody);
        }
        return new Pair<>(FormData.newInstance(), new Object());
    }
}
