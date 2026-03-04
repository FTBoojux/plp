package org.example.leetcode.concatenatedBinary;

class Solution {
    private static final int MOD = 1_000_000_007;
    public int concatenatedBinary(int n) {
        long ans = 0;
        int bit = 1;
        for(int i = 1; i <= n; ++i) {
            while (i >= Math.pow(2,bit)) {
                ++bit;
            }
            ans <<= bit;
            ans += i;
            ans %= MOD;
        }
        return (int)ans;
    }
}