package org.example.nowcoder.HJ58;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt((a)->-a));
        for(int i = 0; i < n; ++i){
            pq.add(in.nextInt());
            while(pq.size() > k){
                pq.poll();
            }
        }
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        while (!pq.isEmpty()){
            deque.add(pq.poll());
        }
        while (!deque.isEmpty()){
            System.out.print(deque.pollLast() + " ");
        }
    }
}
