package org.example.nowcoder.HJ17;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
//    static Set<Character> legalTowards = Set.of('W', 'A', 'S', 'D');
    static Set<Character> legalTowards = new HashSet<>();
    static {
        legalTowards.add('W');
        legalTowards.add('A');
        legalTowards.add('S');
        legalTowards.add('D');
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String[] commands = line.split(";");
        int x = 0, y = 0;
        for (String command : commands) {
            if(command.isEmpty()){
                continue;
            }else{
                try{
                    Pair<Character, Integer> move = legal(command);
                    switch (move.key) {
                        case 'W':
                            y += move.value;
                            break;
                        case 'S':
                            y -= move.value;
                            break;
                        case 'D':
                            x += move.value;
                            break;
                        case 'A':
                            x -= move.value;
                            break;
                    }
                } catch (RuntimeException e) {
                    continue;
                }
            }
        }
        System.out.println(x + "," + y);
    }

    private static Pair<Character, Integer> legal(String command) {
        // 1. only contains one English letter
        // 2. the first letter must be one of `W`/`A`/`S`/`D`
        // 3. after the first character must be an entire number
        if(command.length() < 2) {
            throw new RuntimeException();
        }
        char direction = command.charAt(0);
        if (!legalTowards.contains(direction)) {
            throw new RuntimeException();
        }
        String distanceStr = command.substring(1);
        try {
            int distance = Integer.parseInt(distanceStr);
            return new Pair<>(direction, distance);
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }
    }
    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
