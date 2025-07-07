package org.example.codeforce.DerangedDeletions;

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
        for(int i = 0; i < n; ++i){
            a[i] = in.nextInt();
        }
        for(int i = 0; i < n; ++i){
            for(int j = i + 1; j < n; ++j){
                if(a[i] > a[j]){
                    System.out.println("YES");
                    System.out.println(2);
                    System.out.println(a[i] + " " + a[j]);
                    return;
                }
            }
        }
        System.out.println("NO");
    }
}
