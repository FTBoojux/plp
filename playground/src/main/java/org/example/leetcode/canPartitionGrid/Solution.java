package org.example.leetcode.canPartitionGrid;

public class Solution {
    public boolean canPartitionGrid(int[][] grid) {
        int width = grid.length,
                height = grid[0].length;
        long[][] preSum = new long[width+1][height+1];
        long sum = 0;
        for (int[] ints : grid) {
            for (int anInt : ints) {
                sum += anInt;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                preSum[i+1][j+1] = preSum[i][j+1] + preSum[i+1][j] - preSum[i][j] + grid[i][j];
            }
        }
        for(int i = 1; i < width; ++i) {
            if(preSum[i][height]*2 == sum) {
                return true;
            }
        }
        for(int i = 1; i < height; ++i) {
            if(preSum[width][i]*2 == sum) {
                return true;
            }
        }
        return false;
    }
}