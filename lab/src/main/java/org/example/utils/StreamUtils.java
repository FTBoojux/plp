package org.example.utils;

import java.io.IOException;
import java.io.InputStream;

public final class StreamUtils {
    private StreamUtils(){

    }
    public String readNBytesAsString(InputStream is, int bytes) throws IOException {
//        byte[] bytesRead = ;
        return new String(is.readNBytes(bytes));
    }
}
