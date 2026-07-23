package org.example.nowcoder.HJ45;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        while (n-- > 0) {
            solve(in);
        }
    }

    private static void solve(Scanner in) {
        String name = in.nextLine();
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : name.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0)+1);
        }
        List<Integer> values = map.values().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int num = 26;
        int score = 0;
        for(int value : values ) {
            score += value * num;
            num--;
        }
        System.out.println(score);
    }
}
