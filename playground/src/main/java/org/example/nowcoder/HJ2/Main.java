package org.example.nowcoder.HJ2;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s =  in.nextLine();
        char c = Character.toLowerCase(in.next().charAt(0));
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char ch : s.toCharArray()) {
            ch =  Character.toLowerCase(ch);
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }
        System.out.println(map.getOrDefault(c, 0));
    }
}