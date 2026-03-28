package org.example.store.engine;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SimpleStoreEngineTest {
    public SimpleStoreEngine createStoreEngine() {
        return new SimpleStoreEngine();
    }

    private static final String KEY_FOR_TEST = "key";
    private static final String KEY_FOR_EXPIRED = "keyForExpired";
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
    @Test
    public void set_key_with_expire_time() {
        SimpleStoreEngine storeEngine = getSimpleStoreEngineAndSetTestKey();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        storeEngine.setClock(clock);

        storeEngine.set(KEY_FOR_EXPIRED, VALUE_FOR_TEST, 1, TimeUnit.MINUTES);
        Optional<byte[]> value = storeEngine.get(KEY_FOR_EXPIRED);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(VALUE_FOR_TEST, value.get());
    }
    @Test
    public void should_get_empty_after_key_expired() {
        SimpleStoreEngine storeEngine = getSimpleStoreEngineAndSetTestKey();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        storeEngine.setClock(clock);
        storeEngine.set(KEY_FOR_EXPIRED,VALUE_FOR_TEST,1,TimeUnit.MINUTES);

        Optional<byte[]> value = storeEngine.get(KEY_FOR_EXPIRED);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(VALUE_FOR_TEST, value.get());

        Duration duration_of_one_minute = Duration.ofMinutes(1).plusSeconds(1);
        clock = Clock.offset(clock, duration_of_one_minute);
        storeEngine.setClock(clock);
        value = storeEngine.get(KEY_FOR_EXPIRED);
        Assertions.assertTrue(value.isEmpty());
    }
    @Test
    public void should_remove_from_engine_after_entry_expired() {
        SimpleStoreEngine storeEngine = getSimpleStoreEngineAndSetTestKey();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        storeEngine.setClock(clock);
        storeEngine.set(KEY_FOR_EXPIRED,VALUE_FOR_TEST,1,TimeUnit.MINUTES);

        Optional<byte[]> value = storeEngine.get(KEY_FOR_EXPIRED);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(VALUE_FOR_TEST, value.get());

        Duration duration_of_one_minute = Duration.ofMinutes(1).plusSeconds(1);
        clock = Clock.offset(clock, duration_of_one_minute);
        storeEngine.setClock(clock);
        storeEngine.get(KEY_FOR_EXPIRED);
        Assertions.assertFalse(storeEngine.concurrentHashMap.containsKey(KEY_FOR_EXPIRED));
    }
}
