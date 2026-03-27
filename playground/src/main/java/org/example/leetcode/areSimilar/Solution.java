package org.example.leetcode.areSimilar;

class Solution {
    public boolean areSimilar(int[][] mat, int k) {
        int width = mat.length,
                height = mat[0].length;
        k %= height;
        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                int move = -1;
                if(i % 2 == 0) {
                    move = mat[i][(j+k) % height];
                } else {
                    move = mat[i][(j-k+height) % height];
                }
                if(move != mat[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}