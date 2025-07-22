package org.example.leetcode.MaximumErasureValue;

import java.util.HashSet;
import java.util.Set;

public class Solution {
    public int maximumUniqueSubarray(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int ans = 0, sum = 0;
        for(int left = 0, right = 0; right < nums.length; ++right){
            while(set.contains(nums[right])){
                set.remove(nums[left]);
                sum -= nums[left];
                ++left;
            }
            sum += nums[right];
            set.add(nums[right]);
            ans = Math.max(ans, sum);
        }
        return sum;
    }
}