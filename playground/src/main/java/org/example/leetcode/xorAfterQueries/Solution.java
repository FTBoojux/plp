package org.example.leetcode.xorAfterQueries;

class Solution {
    private static final long MOD = 1_000_000_007;
    public int xorAfterQueries(int[] nums, int[][] queries) {
        for (int[] query : queries) {
            int l = query[0],
                    r = query[1],
                    k = query[2],
                    v = query[3];
            int idx = l;
            while (idx <= r) {
                nums[idx] = (int)(((long)nums[idx] * v) % MOD);
                idx += k;
            }
        }
        int ans = nums[0];
        for(int i = 1; i < nums.length; ++i) {
            ans ^= nums[i];
        }
        return ans;
    }
}