package org.example.leetcode.peopleAwareOfSecret;

public class Solution {
    private static final int MOD = 1_000_000_007;
    public int peopleAwareOfSecret(int n, int delay, int forget) {
        int[] diff = new int[n+1];
        // known 表示有第i天知道消息的人数
        int known = 0;
        diff[1] = 1;
        diff[2] = -1;
        long ans = 0;
        for (int i = 1; i <= n; ++i) {
            known = (known+diff[i]) % MOD;
            if (i >= n-forget+1) {
                ans += known;
            }
            // 在第i+delay天开始传播消息
            if (i+delay <= n) {
                diff[i+delay] = (diff[i+delay]+known)%MOD;
            }
            // 在第i+forget天遗忘
            if (i+forget <= n) {
                diff[i+forget] = (diff[i+forget]-known+MOD)%MOD;
            }
        }
        return (int)(ans%MOD);
    }
}