package org.example.nowcoder.HJ36;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String t = in.nextLine();
        Set<Character> set = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if(!set.contains(ch)){
                set.add(ch);
                sb.append(ch);
            }
        }
        for(int i = 0; i < 26; ++i){
            char cur = (char)('a' + i);
            if(!set.contains(cur)){
                sb.append(cur);
            }
        }
        Map<Character, Character> map = new HashMap<>();
        for(int i = 0; i < 26; ++i) {
            char cur = (char)('a' + i);
            map.put(cur, sb.charAt(i));
        }
        StringBuilder ansSb = new StringBuilder();
        for (char ch : t.toCharArray()) {
            ansSb.append(map.get(ch));
        }
        System.out.println(ansSb.toString());
    }
}
