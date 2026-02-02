package org.example.utils;

import java.util.Collection;
import java.util.Objects;

public final class CollectionUtils {
    private CollectionUtils(){}
    public static <T extends Collection<?>> boolean isEmpty(T collection){
        return Objects.isNull(collection) || collection.isEmpty();
    }
}
