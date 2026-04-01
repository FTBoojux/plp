package org.example.codeforce.c1087.B;

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
        for(int i = 0; i < n; ++i) {
            a[i] = in.nextInt();
        }
        int[] ans = new int[n];
        for(int i = 0; i < n; ++i) {
            int bigger = 0,
                    smaller = 0;
            for(int j = i + 1; j < n; ++j) {
                if(a[j] > a[i]) {
                    ++bigger;
                }
                if(a[j] < a[i]) {
                    ++smaller;
                }
            }
            ans[i] = Math.max(bigger, smaller);
        }
        for (int an : ans) {
            System.out.print(an + " ");
        }
        System.out.println();
    }
}
