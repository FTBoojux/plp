package org.example.codeforce.c1052.B;

import java.util.*;

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
        int n = in.nextInt(), m = in.nextInt();
        Map<Integer, Integer> map = new HashMap<>();
        List<Set<Integer>> sn = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> set = new HashSet<>();
            int l = in.nextInt();
            for (int j = 0; j < l; j++) {
                int s = in.nextInt();
                set.add(s);
                map.put(s,map.getOrDefault(s,0)+1);
            }
            sn.add(set);
        }
        if (map.size() < m) {
            System.out.println("NO");
            return;
        }
        int cnt = 0;
        for (Set<Integer> integers : sn) {
            boolean canRemove = true;
            for (int s : integers) {
                map.put(s,map.get(s)-1);
                if (map.get(s) == 0) {
                    canRemove = false;
                }
            }
            if (canRemove) {
                ++cnt;
                if (cnt == 2) {
                    break;
                }
            }
            for (int s : integers) {
                map.put(s,map.get(s)+1);
            }
        }
        System.out.println(cnt >= 2 ? "YES" : "NO");

    }
}
