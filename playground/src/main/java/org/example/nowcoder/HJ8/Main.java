package org.example.nowcoder.HJ8;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Map<Integer, Integer> map = new HashMap<>();
        while(n-- > 0){
            int index = in.nextInt();
            int value = in.nextInt();
            map.put(index, value + map.getOrDefault(index, 0));
        }
        map.keySet().stream().sorted().forEach(key->{
            System.out.println(key + " "  + map.get(key));
        });
    }
}
