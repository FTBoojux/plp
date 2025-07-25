package org.example.leetcode.maxSum;

import java.util.HashSet;
import java.util.Set;

public class Solution {
    public int maxSum(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int ans = 0,max = Integer.MIN_VALUE;
        for (int num : nums) {
            if(num > 0 && !set.contains(num)){
                set.add(num);
                ans += num;
            }
            max = Math.max(max,num);
        }
        return ans > 0 ? ans : max;
    }
}