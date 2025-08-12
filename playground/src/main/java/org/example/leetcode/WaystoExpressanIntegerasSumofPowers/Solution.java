package org.example.leetcode.WaystoExpressanIntegerasSumofPowers;

public class Solution {
    private static final int MOD = 1_000_000_007;
    public int numberOfWays(int n, int x) {
        int[][] dp = new int[n+1][n+1];
        dp[0][0] = 1;
        for(int i = 1; i <= n; ++i){
            int val = (int)Math.pow(i,x);
            for(int j = 0; j <= n; ++j){
                dp[i][j] = dp[i-1][j];
                if( j - val > 0){
                    dp[i][j] = (dp[i][j] + dp[i-1][j-val])%MOD;
                }
            }
        }
        return dp[n][n];
    }
}