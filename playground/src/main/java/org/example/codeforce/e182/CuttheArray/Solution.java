package org.example.codeforce.e182.CuttheArray;

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
        int[] preSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            preSum[i+1] = preSum[i]+in.nextInt();
        }
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                int left = (preSum[i+1])%3;
                int mid = (preSum[j+1]-preSum[i+1])%3;
                int right = (preSum[n]-preSum[j+1])%3;
                if ((left == mid && left == right)
                        || (left != mid && right != mid && left != right)
                )
                {
                    System.out.println((i+1) + " " + (j+1));
                    return;
                }
            }
        }
        System.out.println("0 0");
    }
}
