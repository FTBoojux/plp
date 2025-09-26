package org.example.leetcode.triangleNumber;

import java.util.Arrays;

public class Solution {
    public int triangleNumber(int[] nums) {
        if (nums.length < 3) {
            return 0;
        }
        int ans = 0;
        Arrays.sort(nums);
        for(int i = 0; i < nums.length-2;++i){
            int k = i + 2;
            for(int j = i + 1; j < nums.length-1 && nums[j] != 0;++j){
                for(;k<nums.length;++k){
                    if (nums[i]+nums[j] <= nums[k]){
                        break;
                    }
                }
                ans += k-j-1;
            }
        }
        return ans;
    }
}