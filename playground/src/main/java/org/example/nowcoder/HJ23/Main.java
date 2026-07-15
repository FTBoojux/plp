package org.example.nowcoder.HJ23;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String string = sc.nextLine();
        solve(string);
    }
    private static void solve(String string){
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : string.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }
        int minTimes = Integer.MAX_VALUE;
        for (Integer value : map.values()) {
            minTimes = Math.min(minTimes, value);
        }
        StringBuilder sb = new StringBuilder();
        for (char ch : string.toCharArray()) {
            if (map.get(ch) != minTimes) {
                sb.append(ch);
            }
        }
        System.out.println(sb.toString());
    }
}
