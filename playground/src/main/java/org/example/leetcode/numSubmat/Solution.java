package org.example.leetcode.numSubmat;

public class Solution {
    public int numSubmat(int[][] mat) {
        int m = mat.length,
                n = mat[0].length;
        int[][] rows = new int[m][n];
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    rows[i][j] = mat[i][j];
                } else {
                    rows[i][j] = mat[i][j] == 0 ? 0 : rows[i][j-1] + 1;
                }
                int cur = rows[i][j];
                for (int k = i; k >= 0; k--) {
                    cur = Math.min(cur, rows[k][j]);
                    ans += cur;
                    if (cur == 0) {
                        break;
                    }
                }
            }
        }
        return ans;
    }
}