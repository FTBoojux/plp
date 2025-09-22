//package org.example.codeforce.c1052.A;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        int t = in.nextInt();
        Solution solution = new Solution();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public void solve(Scanner in) {
        int n = in.nextInt();
        int[] a = new int[n];
        int maxFrequent = 0;
        Map<Integer,Integer> frequent = new HashMap<>();
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            frequent.put(a[i],frequent.getOrDefault(a[i],0)+1);
            maxFrequent = Math.max(maxFrequent,frequent.get(a[i]));
        }
        Map<Integer,Integer> frqMap = new HashMap<>();
        for (Integer freq : frequent.values()) {
            frqMap.put(freq,frqMap.getOrDefault(freq,0)+1);
        }
        int cnt = 0,ans = 0;
        for (int i = maxFrequent; i > 0; --i) {
            cnt += frqMap.getOrDefault(i,0);
            ans = Math.max(ans,cnt*i);
        }
        System.out.println(ans);
    }
}
