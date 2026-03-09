package org.example.POJO;

public record TlvMessage<T>(byte type,int length,T value) {
}
