package org.example.leetcode.rangeAddQueries;

public class Solution {
    public int[][] rangeAddQueries(int n, int[][] queries) {
        int[][] diff = new int[n+1][n+1];
        for (int[] query : queries) {
            int row1 = query[0],
                    row2 = query[2],
                    col1 = query[1],
                    col2 = query[3];
            ++diff[row1][col1];
            --diff[row1][col2+1];
            --diff[row2+1][col1];
            ++diff[row2+1][col2+1];
        }
        int[][] ans = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x1 = i == 0 ? 0 : ans[i-1][j];
                int x2 = j == 0 ? 0 : ans[i][j-1];
                int x3 = (i == 0 || j == 0) ? 0 : ans[i-1][j-1];
                ans[i][j] = diff[i][j] + x1 + x2 - x3;
            }
        }
        return ans;
    }
}
