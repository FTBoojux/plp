package org.example.codeforce.c1091.A;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        Solution solution = new Solution();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public void solve(Scanner in) {
        int n = in.nextInt(),
                k = in.nextInt();
        int sum = 0;
        for (int i = 0; i < n; ++i) {
            sum += in.nextInt();
        }
        if (sum % 2 == 1 || (n * k) % 2 == 0) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
