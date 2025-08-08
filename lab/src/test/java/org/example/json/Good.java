package org.example.json;

import java.util.Objects;

public class Good {
    private String id;
    private double price;

    public Good(String id, double price) {
        this.id = id;
        this.price = price;
    }
    public Good(){

    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Good[" +
               "id=" + id + ", " +
               "price=" + price + ']';
    }
}
