package org.example.nowcoder.HJ59;

import java.util.*;

public class Main {
    final static char FAULT = '-';
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char ans = FAULT;
        char[] charArray = s.toCharArray();
        Map<Character, Integer> cnt = new HashMap<>();
        ArrayDeque<Character> deque = new ArrayDeque<>();
        for(int i = charArray.length-1; i >=0; --i){
            char cur = charArray[i];
            deque.addFirst(cur);
            cnt.put(cur, cnt.getOrDefault(cur,0)+1);
            while(!deque.isEmpty() && cnt.getOrDefault(deque.getFirst(),0) > 1){
                deque.pollFirst();
            }
        }
        System.out.println(deque.isEmpty() ? "-1" : deque.getFirst());
    }
}
