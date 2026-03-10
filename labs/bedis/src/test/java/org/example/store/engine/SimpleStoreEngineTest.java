package org.example.store.engine;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class SimpleStoreEngineTest {
    public SimpleStoreEngine createStoreEngine() {
        return new SimpleStoreEngine();
    }

    private static final String KEY_FOR_TEST = "key";
    private static final String KEY_NOT_EXISTED = "keyNotExisted";
    private static final byte[] VALUE_FOR_TEST = "value".getBytes(StandardCharsets.UTF_8);
    private static final byte[] VALUE_FOR_REPLICATE_SET = "value2".getBytes(StandardCharsets.UTF_8);

    @Test
    public void set() {
        getSimpleStoreEngineAndSetTestKey();
    }

    @Test
    public void should_get_same_value_by_the_same_key() {
        SimpleStoreEngine storeEngine = getSimpleStoreEngineAndSetTestKey();
        Optional<byte[]> value = storeEngine.get(KEY_FOR_TEST);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(
                VALUE_FOR_TEST,
                value.get(),
                "value got should equal to 'value'");
    }

    private @NonNull SimpleStoreEngine getSimpleStoreEngineAndSetTestKey() {
        SimpleStoreEngine storeEngine = createStoreEngine();
        storeEngine.set(
                KEY_FOR_TEST,
                VALUE_FOR_TEST
        );
        return storeEngine;
    }

    @Test
    public void should_get_empty_by_key_not_existed() {
        SimpleStoreEngine simpleStore= getSimpleStoreEngineAndSetTestKey();
        Optional<byte[]> emptyValue = simpleStore.get(KEY_NOT_EXISTED);
        Assertions.assertTrue(emptyValue.isEmpty());
    }
    @Test
    public void should_return_new_value_when_using_the_same_key() {
        SimpleStoreEngine storeEngine = getSimpleStoreEngineAndSetTestKey();
        storeEngine.set(KEY_FOR_TEST, VALUE_FOR_REPLICATE_SET);
        Optional<byte[]> value = storeEngine.get(KEY_FOR_TEST);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(VALUE_FOR_REPLICATE_SET,value.get());
    }
}
