package org.example.codeforce.c1076.C;

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
                q = in.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = in.nextInt();
        }
        int[][] queries = new int[q][2];
        for (int[] query : queries) {
            query[0] = in.nextInt();
            query[1] = in.nextInt();
        }
        a[n-1] = Math.max(a[n-1], b[n-1]);
        for(int i = n-2; i >=0; --i) {
            a[i] = Math.max(a[i], Math.max(a[i+1], b[i]));
        }
        int[] preSum = new int[n+1];
        for (int i = 0; i < n; i++) {
            preSum[i+1] = preSum[i] + a[i];
        }
        for (int[] query : queries) {
            System.out.print(preSum[query[1]]-preSum[query[0]-1] + " ");
        }
        System.out.println();
    }
}
