package org.example.leetcode.minimumArea1;

public class Solution {
    public int minimumArea(int[][] grid) {
        int height = grid.length,
                width = grid[0].length;
        int left = width,
                right = 0,
                bottom = height,
                top = 0;
        int cnt = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == 1) {
                    left = Math.min(left,j);
                    right = Math.max(right,j);
                    bottom = Math.min(bottom,i);
                    top = Math.max(top,i);
                    ++cnt;
                }
            }
        }
        return cnt == 0 ? 0 : (right-left+1) * (top - bottom + 1);
    }
}