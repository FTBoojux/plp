package org.example.leetcode.findMaxForm;

public class Solution {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m+1][n+1];
        int ans = 0;
        for (String str : strs) {
            int[] cnt = count(str);
            for (int i = m - cnt[0]; i >= 0; --i) {
                for (int j = n - cnt[1]; j >= 0; --j) {
                    if (dp[i][j] != 0) {
                        int ni = i + cnt[0],
                                nj = j + cnt[1];
                        dp[ni][nj] = Math.max(dp[ni][nj],dp[i][j]+1);
                    }
                }
            }
            if (cnt[0] <= m && cnt[1] <= n) {
                dp[cnt[0]][cnt[1]] = Math.max(dp[cnt[0]][cnt[1]], 1);
            }
        }
        for (int[] ints : dp) {
            for (int anInt : ints) {
                ans = Math.max(ans,anInt);
            }
        }
        return ans;
    }
    private int[] count(String str) {
        int[] cnt = {0,0};
        for (char c : str.toCharArray()) {
            if (c == '0') {
                cnt[0] ++;
            } else {
                cnt[1] ++;
            }
        }
        return cnt;
    }
}
