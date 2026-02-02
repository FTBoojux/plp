package org.example;

import java.util.ArrayList;
import java.util.List;

public class CastTest {
    public static void main(String[] args) {
        Object _true = false;
        List<Object> list = new ArrayList<>();
        list.add(_true);
        System.out.println(list.getFirst() instanceof Boolean);
    }
}
