package org.example.store.engine;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleStoreEngine implements StoreEngine{
    ConcurrentHashMap<String, StorageEntry> concurrentHashMap = new ConcurrentHashMap<>();
    @Override
    public boolean set(String key, byte[] value) {
        if (Objects.isNull(value)) {
            return false;
        }
        StorageEntry entry = new StorageEntry(value, -1);
        concurrentHashMap.put(key, entry);
        return true;
    }

    @Override
    public boolean set(String key, byte[] value, long expireTime, long timeUnit) {
        return false;
    }

    @Override
    public Optional<byte[]> get(String key) {
        StorageEntry valueEntry = concurrentHashMap.get(key);
        if (Objects.isNull(valueEntry)) {
            return Optional.empty();
        } else {
            return Optional.of(valueEntry.value());
        }
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean exist(String key) {
        return false;
    }
}
