package org.example.codeforce.MinimiseSum;

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
        int[] a =  new int[n];
        for(int i = 0; i < n; ++i){
            a[i] = in.nextInt();
        }
        System.out.println(a[0] > a[1] ? a[0]+a[1] : 2*a[0]);
    }
}
