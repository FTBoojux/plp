package org.example.codeforce.c1083.A;

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
        int[] p = new int[n];
        int maxIndex = 0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            p[i] = in.nextInt();
            if (p[i] > p[maxIndex]) {
                maxIndex = i;
            }
            if (p[maxIndex] == i+1) {
                ++count;
            }
        }
        if(count > 1) {
            int temp = p[0];
            p[0] = p[maxIndex];
            p[maxIndex] = temp;
        }
        for (int i = 0; i < n; i++) {
            System.out.print(p[i] + " ");
        }
        System.out.println();
    }
}
