package org.example.codeforce.c1074.A;

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
        for(int i = 1; i <= n; ++i) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
