package org.example.store.engine;

import java.util.Optional;

public interface StoreEngine {
    boolean set(String key, byte[] value);
    boolean set(String key, byte[] value, long expireTime, long timeUnit);
    Optional<byte[]> get(String key);
    boolean delete(String key);
    boolean exist(String key);
}
