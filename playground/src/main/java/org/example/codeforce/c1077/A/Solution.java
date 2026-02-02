package org.example.codeforce.c1077.A;

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
        int[] p = new int[n];
        p[n-1] = n;
        int diff = n-1,
                mod = -1;
        for(int i = 1; n-1-i >= 0; ++i) {
            p[n-1-i] = p[n-i]+(diff*mod);
            --diff;
            mod *= -1;
        }
        for (int j : p) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}
