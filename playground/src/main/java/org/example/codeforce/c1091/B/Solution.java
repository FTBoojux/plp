package org.example.codeforce.c1091.B;

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
                k = in.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }
        int p = in.nextInt();
        int max = Integer.MIN_VALUE;
        int cnt = 0;
        for(int i = p - 2; i >= 0; i--){
            if(a[i] != a[i+1]) {
                ++cnt;
            }
        }
        max = Math.max(max, cnt);
        cnt = 0;
        for(int i = p; i < n; ++i) {
            if(a[i] != a[i-1]) {
                ++cnt;
            }
        }
        max  = Math.max(max, cnt);
        if (max%2 == 1) {
            ++max;
        }
        System.out.println(max);
    }
}
