package org.example.store.engine;

public record StorageEntry(byte[] value, long expireTime) {
}
