package org.example.codeforce.Tournament;

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
        int n =  in.nextInt(),
                j = in.nextInt(),
                k = in.nextInt();
        int[] a = new int[n];
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < n; i++){
            a[i] = in.nextInt();
            max = Math.max(a[i], max);
        }
        if( k > 1 || a[j-1] >= max){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }
    }
}
