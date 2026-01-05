package org.example.leetcode.maxMatrixSum;

class Solution {
    public long maxMatrixSum(int[][] matrix) {
        long ans = 0;
        int min = 100_001;
        int negativeCount = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                ans += Math.abs(num);
                if(num < 0) {
                    ++negativeCount;
                }
                min = Math.min(min,Math.abs(num));
            }
        }
        if(negativeCount % 2 == 1) {
            ans -= min * 2L;
        }
        return ans;
    }
}