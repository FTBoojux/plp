package org.example.web.utils.io;

import org.example.utils.Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public final class BoundarySplitter {
    private BoundarySplitter(){}
    public static void splitToOutputs(InputStream inputStream,
                                      byte[] boundary,
                                      IntFunction<OutputStream> partSupplier) throws IOException {
        int boundaryLength = boundary.length;
        if (boundaryLength == 0) {
            throw new IllegalArgumentException("Boundary can't be empty!");
        }
        int[] lps = buildLps(boundary);

        byte[] cache = new byte[boundaryLength];
        int head = 0, count = 0;

        int matchedPrefixLength = 0;
        int index = 0;
        OutputStream outputStream = partSupplier.apply(index++);
        byte[] buffer = new byte[8096];
        int n;

        while ((n = inputStream.read(buffer)) != -1) {
            for (int k = 0; k < n; k++) {
                byte bt = buffer[k];
                while ( matchedPrefixLength > 0 && bt != boundary[matchedPrefixLength]) {
                    matchedPrefixLength = lps[matchedPrefixLength-1];
                }
                if (boundary[matchedPrefixLength] == bt) {
                    ++matchedPrefixLength;
                }

                cache[(head+count) % boundaryLength] = bt;
                ++count;

                if (matchedPrefixLength == boundaryLength) {
                    head = (head+count) % boundaryLength;
                    count = 0;
                    outputStream.flush();
                    outputStream.close();
                    outputStream = partSupplier.apply(index++);

                    matchedPrefixLength = 0;
                } else {
                    while (count > matchedPrefixLength) {
                        outputStream.write(cache[head]);
                        head = (head+1) % boundaryLength;
                        --count;
                    }
                }
            }
        }
        outputStream.flush();
        outputStream.close();
    }

    public static List<byte[]> splitStreamIntoByteArray(InputStream inputStream, byte[] boundary) throws IOException {
        List<byte[]> parts = new ArrayList<>();
        splitToOutputs(inputStream,boundary,idx->new ByteArrayOutputStream(){
            @Override
            public void close() throws IOException {
                super.close();
                parts.add(this.toByteArray());
            }
        });
        return parts;
    }
    public static List<byte[]> splitByteArrayByBoundary(byte[] input, byte[] boundary) {
        int boundaryLength = boundary.length;
        if (boundaryLength == 0) {
            throw new IllegalArgumentException("Boundary can't be empty!");
        }
        int[] lps = buildLps(boundary);

        byte[] cache = new byte[boundaryLength];
        int head = 0, count = 0;
        int matchedPrefixLength = 0;
        int index = 0;
        List<Byte> output = new ArrayList<>();
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            byte bt = input[i];
            while (matchedPrefixLength > 0 && bt != boundary[matchedPrefixLength]) {
                matchedPrefixLength = lps[matchedPrefixLength-1];
            }
            if (boundary[matchedPrefixLength] == bt) {
                ++matchedPrefixLength;
            }
            cache[(head+count) % boundaryLength] = bt;
            ++count;

            if (matchedPrefixLength == boundaryLength) {
                head = (head+count) % boundaryLength;
                count = 0;
                result.add(Bytes.toArray(output));
                output.clear();
                matchedPrefixLength = 0;
            } else {
                while (count > matchedPrefixLength) {
                    output.add(cache[head]);
                    head = (head+1) % boundaryLength;
                    --count;
                }
            }
        }
        result.add(Bytes.toArray(output));
        return result;
    }
    private static int[] buildLps(byte[] pattern) {
        int patternLength = pattern.length;
        int[] lps = new int[patternLength];
        int patternIndex = 0;
        for (int i = 1; i < patternLength; ++i) {
            while (patternIndex > 0 && pattern[i] != pattern[patternIndex]) {
                patternIndex = lps[patternIndex-1];
            }
            if (pattern[patternIndex] == pattern[i]) {
                ++patternIndex;
            }
            lps[i] = patternIndex;

        }
        return lps;
    }
}
