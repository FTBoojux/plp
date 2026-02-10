package org.example.leetcode.longestBalanced;

import java.util.HashSet;
import java.util.Set;

class Solution {
    public int longestBalanced(int[] nums) {
        Set<Integer> even = new HashSet<>(),
                odd = new HashSet<>();
        int ans = 0;
        int size = nums.length;
        for(int i = 0; i < size; ++i) {
            even.clear();
            odd.clear();
            for( int j = i; j < size; ++j) {
                if (nums[j] % 2 == 0) {
                    even.add(nums[j]);
                } else {
                    odd.add(nums[j]);
                }
                if (even.size() == odd.size()) {
                    ans = Math.max(ans, j - i + 1);
                }
            }
        }
        return ans;
    }
}