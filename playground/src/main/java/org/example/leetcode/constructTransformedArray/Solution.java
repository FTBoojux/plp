package org.example.leetcode.constructTransformedArray;

class Solution {
    public int[] constructTransformedArray(int[] nums) {
        int length = nums.length;
        int[] ans = new int[length];
        for(int i = 0; i < length; ++i) {
            int j = (i + ans[i]) % length;
            while (j < 0) {
                j += length;
            }
            ans[i] = nums[j];
        }
        return ans;
    }
}