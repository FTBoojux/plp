package org.example.codeforce.c1097.A;

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
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = in.nextLong();
        }
        long ans = 0;
        for(int i = n-2; i >= 0; i--) {
            long sum = arr[i + 1] + arr[i];
            if(sum > arr[i]) {
                arr[i] = sum;
            }
        }
        int cnt = 0;
        for (long l : arr) {
            if(l > 0) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }
}
