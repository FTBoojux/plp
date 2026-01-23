package org.example.codeforce.c1074.B;

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
        int max = 0;
        for (int i = 0; i < n; i++) {
            int a = in.nextInt();
            max = Math.max(max,a);
        }
        System.out.println(max*n);
    }
}
