package org.example.codeforce.c1076.A;

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
        int n = in.nextInt(),
                s = in.nextInt(),
                x = in.nextInt();
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += in.nextInt();
        }
        int diff = s - sum;
        if (diff >= 0 && (diff % x) == 0) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
