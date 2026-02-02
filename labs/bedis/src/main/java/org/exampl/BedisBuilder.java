package org.exampl;

public class BedisBuilder {
    private int port = 6379;
    protected BedisBuilder(){

    }
    public BedisBuilder port(int port) {
        this.port = port;
        return this;
    }
    public Bedis build() {
        return new Bedis(port);
    }
}
