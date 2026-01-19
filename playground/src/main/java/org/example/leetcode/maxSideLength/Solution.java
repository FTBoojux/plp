package org.example.leetcode.maxSideLength;

class Solution {
    public int maxSideLength(int[][] mat, int threshold) {
        int height = mat.length,
                width = mat[0].length;
        int[][] preSum = new int[height+1][width+1];
        for (int i = 0; i < height; i++) {
            preSum[i+1][1] = preSum[i][1] + mat[i][0];
        }
        for (int i = 0; i < width; i++) {
            preSum[1][i+1] = preSum[1][i] + mat[0][i];
        }
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                preSum[i+1][j+1] = preSum[i][j+1] + preSum[i+1][j] - preSum[i][j] + mat[i][j];
            }
        }
        int left = 1,
                right = Math.min(height, width);
        while (left <= right) {
            int mid = (left+right)/2;
            if (!ok(preSum, threshold, mid)) {
                right = mid -1;
            } else {
                left = mid+1;
            }
        }
        return left-1;
    }

    private boolean ok(int[][] preSum, int threshold, int target) {
        int height = preSum.length,
                width = preSum[0].length;
        for(int i = 0; i+target < height; ++i) {
            for (int j = 0; j + target < width; ++j) {
                int sum = preSum[i+target][j+target]
                        - preSum[i][j+target]
                        -preSum[i+target][j]
                        +preSum[i][j];
                if (sum <= threshold) {
                    return true;
                }
            }
        }
        return false;
    }
}