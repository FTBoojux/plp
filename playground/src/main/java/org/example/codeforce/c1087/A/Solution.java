package org.example.codeforce.c1087.A;

import java.util.Arrays;
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
        int n,k;
        long c;
        n = in.nextInt();
        c = in.nextInt();
        k = in.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }
        Arrays.sort(a);
        for (int _a : a) {
            long diff = c - _a;
            if(diff < 0) {
                break;
            } else {
                long addPower = Math.min(diff, k);
                k -= addPower;
                c += _a + addPower;
            }
        }
        System.out.println(c);
    }
}
