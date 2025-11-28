package org.example.codeforce.c1066.A;

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
        Map<Integer, Integer> count = new HashMap<>();
        int ans = 0;
        while (n-- > 0) {
            int num = in.nextInt();
            count.put(num, count.getOrDefault(num,0)+1);
            int current = count.get(num);
            if (current < num) {
                ++ans;
            } else if (current > num) {
                ++ans;
            } else if (current == num && current != 1) {
                ans -= num-1;
            }
        }
        System.out.println(ans);
    }
}
