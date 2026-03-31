package org.example.store.engine;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ComputeSimpleStoreEngine implements StoreEngine {
    private Clock clock = Clock.systemDefaultZone();
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
    public boolean set(String key, byte[] value, long expireTime, TimeUnit timeUnit) {
        Duration duration = Duration.of(expireTime, timeUnit.toChronoUnit());
        Clock expireAt = Clock.offset(clock, duration);
        StorageEntry entry = new StorageEntry(value, expireAt.millis());
        concurrentHashMap.put(key, entry);
        return true;
    }

    @Override
    public Optional<byte[]> get(String key) {
        StorageEntry entry = concurrentHashMap.compute(key, (k, oldEntry) -> {
            if (oldEntry == null) {
                return null;
            }
            if (expired(oldEntry)) {
                return null; // 返回null的话，在操作结束后一起删除这个entry
            }
            return oldEntry; // 返回不是null的值，表示维持已有的值
        });

        return entry == null
                ? Optional.empty()
                : Optional.of(entry.value());
    }

    private boolean expired(StorageEntry valueEntry) {
        if(valueEntry == null) {
            return true;
        }
        return valueEntry.hasExpire() && valueEntry.expireTime() < clock.millis();
    }

    @Override
    public boolean delete(String key) {
        return concurrentHashMap.remove(key) != null;
    }

    @Override
    public boolean exist(String key) {
        return false;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
