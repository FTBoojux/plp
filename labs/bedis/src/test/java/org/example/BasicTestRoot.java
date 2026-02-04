package org.example;

import org.junit.jupiter.api.BeforeEach;

import java.util.Objects;

public abstract class BasicTestRoot {
    protected volatile Bedis bedis;
    private int PORT = 6379;
    @BeforeEach
    public void setUp() {
        new Thread(() -> {
            bedis = Bedis.builder()
                    .port(PORT)
                    .build();
            bedis.run();
        }).start();
        while (Objects.isNull(bedis)){};
    }
}
