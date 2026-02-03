package org.example.leetcode.isTrionic;

class Solution {
    public boolean isTrionic(int[] nums) {
        int top = 0,
                bottom = 0;
        for(int i = 1; i + 1 < nums.length; ++i) {
            if (nums[i-1] == nums[i] || nums[i+1] == nums[i]) {
                return false;
            }
            if (nums[i-1] < nums[i] && nums[i+1] < nums[i]) {
                ++top;
            } else if (nums[i-1] > nums[i] && nums[i+1] > nums[i]) {
                ++bottom;
            }
        }
        return top == 1 && bottom == 1 && nums[1] > nums[0];
    }
}