package org.example.web.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.IntFunction;

public final class InputStreamBoundarySplitter {
    private InputStreamBoundarySplitter(){}
    public static void splitToOutputs(InputStream inputStream,
                                      byte[] boundary,
                                      IntFunction<OutputStream> partSupplier) throws IOException {
        int boundaryLength = boundary.length;
        if (Objects.isNull(boundary) || boundaryLength == 0) {
            throw new IllegalArgumentException("Boundary can't be null!");
        }
        int[] lps = buildLps(boundaryLength);

        byte[] cache = new byte[boundaryLength];
        int head = 0, count = 0;

        int matchedPrefixLength = 0;
        int index = 0;
        OutputStream outputStream = partSupplier.apply(index++);
        byte[] buffer = new byte[8096];
        int n = 0;


    }

    private static int[] buildLps(int boundaryLength) {

    }
}
