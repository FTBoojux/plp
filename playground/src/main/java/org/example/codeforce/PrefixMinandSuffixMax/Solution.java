package org.example.codeforce.PrefixMinandSuffixMax;

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
        int[] a = new int[n],
            preMin = new int[n],
            suffixMax = new int[n];
        int min = Integer.MAX_VALUE,
            max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
            min = Math.min(min, a[i]);
            preMin[i] = min;
        }
        for (int i = 0; i < n; i++) {
            max = Math.max(max, a[n-i-1]);
            suffixMax[n-i-1] = max;
        }
        for (int i = 0; i < n; i++) {
            if(preMin[i] >= a[i] || suffixMax[i] <= a[i]){
                System.out.print(1);
            }else{
                System.out.print(0);
            }
        }
        System.out.println();
    }
}
