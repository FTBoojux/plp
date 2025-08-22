package org.example.web.multipart;

import org.example.utils.Fog;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MultipartParser {
    private static final int chunkSize = 8096;
    byte[] boundary;
    byte[] chunk = new byte[chunkSize];
    int[] kmpTable;
    int kmpState = 0;
    byte[] overlap;
    int limit;
    public MultipartParser(byte[] content, String boundary){
        byte[] originalBytes = boundary.getBytes(StandardCharsets.UTF_8);
        byte[] real_bytes = new byte[originalBytes.length + 2];
        real_bytes[0] = '-';
        real_bytes[1] = '-';
        System.arraycopy(originalBytes, 0, real_bytes, 2, originalBytes.length);
        this.boundary = real_bytes;

    }
    public ParserResult processChunk(){
        ParserResult parserResult = new ParserResult();

        return parserResult;
    }
}
