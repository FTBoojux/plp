package org.example.nowcoder.HJ27;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = sc.next();
        }
        String x = sc.next();
        int k = sc.nextInt();
        Map<Character, Integer> map = new HashMap<>();
        for (char ch : x.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }
        List<String> list = new ArrayList<>();
        for (String string : s) {
            if(!string.equals(x)){
                if(isBro(string, map)){
                    list.add(string);
                }
            }
        }
        System.out.println(list.size());
        Collections.sort(list);
        if(list.size() >= k){
            System.out.println(list.get(k-1));
        }
    }
    private static boolean isBro(String string, Map<Character, Integer> target){
        Map<Character, Integer> stringMap = new HashMap<>();
        for (char ch : string.toCharArray()) {
            stringMap.put(ch, stringMap.getOrDefault(ch, 0) + 1);
        }
        return stringMap.equals(target);
    }
}
