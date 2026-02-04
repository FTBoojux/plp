package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.BindException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BedisTest {
    private volatile Bedis bedis;
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
    @Test
    public void listenOnPortProvided(){
        bedis.stop();
    }
    @Test()
    public void listenOnSamePort_shouldThrowException(){
        assertThrows(BindException.class,()-> {
            Bedis.builder()
                    .port(PORT)
                    .build()
                    .run();
        },"should throw BindException when use bound port");
    }

}