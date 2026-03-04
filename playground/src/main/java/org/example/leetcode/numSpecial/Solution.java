package org.example.leetcode.numSpecial;

class Solution {
    public int numSpecial(int[][] mat) {
        int height = mat.length,
                width = mat[0].length;
        int[] rows = new int[height];
        int[] cols = new int[width];
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                if(mat[i][j] == 1) {
                    ++rows[i];
                    ++cols[j];
                }
            }
        }
        int ans = 0;
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                if(mat[i][j] == 1 && rows[i] == 1 && cols[j] == 1) {
                    ++ans;
                }
            }
        }
        return ans;
    }
}