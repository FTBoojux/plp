package org.example.store.engine.cleanup;

import org.example.store.engine.SimpleStoreEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SimpleStoreEngineCleanUpTest {
    private static final String PERMANENT_KEY = "key";
    private static final byte[] PERMANENT_VALUE = "value".getBytes();
    private static final String UNEXPIRED_KEY = "unexpired";
    private static final byte[] UNEXPIRED_VALUE = "unexpiredValue".getBytes();
    private static final String EXPIRED_KEY = "expired";
    private static final byte[] EXPIRED_VALUE = "expiredValue".getBytes();
    @Test
    public void simple_store_engine_can_clean_up() {
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        storeEngine.cleanup();
    }
    @Test
    public void permanent_key_should_not_be_cleanup() {
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        storeEngine.set(PERMANENT_KEY, PERMANENT_VALUE);
        storeEngine.cleanup();
        Optional<byte[]> value = storeEngine.get(PERMANENT_KEY);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(PERMANENT_VALUE, value.get());
    }
    @Test
    public void unexpired_value_should_not_be_cleanup() {
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        storeEngine.setClock(clock);
        storeEngine.set(UNEXPIRED_KEY,
                UNEXPIRED_VALUE,
                        1,
                        TimeUnit.MINUTES);

        storeEngine.cleanup();

        Optional<byte[]> value = storeEngine.get(UNEXPIRED_KEY);
        Assertions.assertTrue(value.isPresent());
        Assertions.assertArrayEquals(UNEXPIRED_VALUE, value.get());
    }
    @Test
    public void expired_value_should_be_cleanup() {
        SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        storeEngine.setClock(clock);
        storeEngine.set(
                EXPIRED_KEY,
                EXPIRED_VALUE,
                1,
                TimeUnit.MINUTES
        );
        clock = Clock.offset(clock, Duration.ofMinutes(1).plusSeconds(1));

        storeEngine.setClock(clock);
        storeEngine.cleanup();

        Optional<byte[]> value = storeEngine.get(EXPIRED_KEY);
        Assertions.assertTrue(value.isEmpty());
    }
}
