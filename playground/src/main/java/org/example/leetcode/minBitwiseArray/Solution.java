package org.example.leetcode.minBitwiseArray;

import java.util.Arrays;
import java.util.List;

class Solution {
    public int[] minBitwiseArray(List<Integer> nums) {
        int size = nums.size();
        int[] ans = new int[size];
        Arrays.fill(ans,-1);
        for (int i = 0; i < size; i++) {
            int num = nums.get(i);
            for(int j = 1; j < num; ++j) {
                if ((j | (j+1)) == num) {
                    ans[i] = j;
                    break;
                }
            }
        }
        return ans;
    }
}