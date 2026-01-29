package org.example.codeforce.c1076.B;

import java.util.ArrayDeque;
import java.util.Deque;
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
        int[] maxAfter = new int[n];
        for (int i = 0; i < n; i++) {
            maxAfter[i] = i;
        }
        Deque<Integer> stk = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            p[i] = in.nextInt();
            while (!stk.isEmpty() && p[stk.peekFirst()] < p[i]) {
                maxAfter[stk.peekFirst()] = i;
                stk.pollFirst();
            }
            stk.addFirst(i);
        }
        for (int i = 0; i < n; i++) {
            int maxIndex = i;
            while (maxAfter[maxIndex] != maxIndex) {
                maxIndex = maxAfter[maxIndex];
            }
            if(maxIndex != i) {
                reverse(p,i,maxIndex);
                break;
            }
        }
        for (int i : p) {
            System.out.print(i +" ");
        }
        System.out.println();
    }

    private void reverse(int[] p, int i, int j) {
        for(int k = 0; k <= (j-i)/2; ++k) {
            int temp = p[i+k];
            p[i+k] = p[j-k];
            p[j-k] = temp;
        }
    }
}
