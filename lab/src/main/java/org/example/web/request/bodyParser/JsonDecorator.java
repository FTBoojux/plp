package org.example.web.request.bodyParser;

import org.example.utils.StringUtils;
import org.example.utils.json.Bson;
import org.example.utils.json.TypeReference;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;
import org.example.web.utils.web.MatchResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 对应application/json类型
 */
public class JsonDecorator implements BodyParser {
    private final Map<RequestHandler<?>, TypeReference> requestBodyClzMap = new HashMap<>();

    @Override
    public void extractBodyData(String rawBody, HttpRequest<Object> httpRequest, MatchResult matchResult) {
        RequestHandler<?> requestHandler = matchResult.requestHandler;
        if (!requestBodyClzMap.containsKey(requestHandler)){
            requestBodyClzMap.put(requestHandler, getRequestClass(requestHandler));
        }
        TypeReference typeReference = requestBodyClzMap.get(requestHandler);
        if (Void.class != typeReference.getType() && StringUtils.isEmpty(rawBody)){
            Object requestBody = Bson.deserializeFromJson(rawBody, typeReference);
            httpRequest.setBody(requestBody);
        }
    }
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
}
