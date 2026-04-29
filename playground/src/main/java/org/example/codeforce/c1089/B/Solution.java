package org.example.codeforce.c1089.B;

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
        int cnt = 0;
        for(int i = 0; i < n; ++i) {
            int p = in.nextInt();
            if(p <= i + 1) {
                ++cnt;
            }
        }
        System.out.println(cnt);
    }
}
