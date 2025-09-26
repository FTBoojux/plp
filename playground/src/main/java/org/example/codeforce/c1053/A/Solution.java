package org.example.codeforce.c1053.A;

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
        int n = in.nextInt(), m  = in.nextInt();
        int[] a = new int[m];
        int ans = 0;
        boolean sorted = true;
        for (int i = 0; i < m; i++) {
            a[i] = in.nextInt();
            if (i > 0 && a[i] <= a[i-1]) {
                sorted = false;
            }
        }
        if (sorted) {
            System.out.println(n-a[m-1]+1);
        } else {
            System.out.println(1);
        }
    }
}
