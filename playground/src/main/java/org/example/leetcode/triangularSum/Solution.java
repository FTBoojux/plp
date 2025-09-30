package org.example.leetcode.triangularSum;

public class Solution {
    public int triangularSum(int[] nums) {
        while (nums.length > 1) {
            int[] nextLevel = new int[nums.length-1];
            for (int i = 0; i < nextLevel.length; i++) {
                nextLevel[i] = (nums[i] + nums[i+1])%10;
            }
            nums = nextLevel;
        }
        return nums[0];
    }
}