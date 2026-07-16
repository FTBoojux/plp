package org.example.nowcoder.HJ24;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = sc.nextInt();
        }
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left,1);
        Arrays.fill(right, 1);
        for (int i = 0; i < n; ++i) {
            for (int j = i -1; j >= 0; --j) {
                if(heights[j] < heights[i]) {
                    left[i] = Math.max(left[i], left[j] + 1);
                }
            }
        }
        for (int i = n-1; i >=0; --i) {
            for (int j = i +1; j < n; ++j){
                if(heights[j] < heights[i]) {
                    right[i] = Math.max(right[i], right[j] + 1);
                }
            }
        }
        int ans = 0;
        for(int i = 0; i < n; ++i) {
            ans = Math.max(ans, left[i] + right[i] - 1);
        }
        System.out.println(n-ans);
    }
}
