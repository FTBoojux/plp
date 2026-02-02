package org.example.web.biew;

import org.example.utils.StringUtils;

import java.io.*;
import java.util.Objects;

public class Biew {
    private String path;
    public Biew(String relativePath) {
        this.path = relativePath;
    }

    public String parse() {
        if (StringUtils.isEmpty(path)) {
            throw new NullPointerException("path in Biew is empty!");
        }
        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        if (Objects.isNull(is)) {
            throw new NullPointerException(String.format("Failed to get system resource : %s",path));
        }
        try {
            return new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
