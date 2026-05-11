package org.example.leetcode.separateDigits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public int[] separateDigits(int[] nums) {
        List<Integer> list = new ArrayList<>();
        int size = nums.length;
        for(int i = size-1; i >=0; --i) {
            int num = nums[i];
            while (num > 0) {
                list.add(num % 10);
                num /= 10;
            }
        }
        int[] ans = new int[list.size()];
        for(int i = 0; i < list.size(); ++i) {
            ans[i] = list.get(list.size() - 1 - i);
        }
        return ans;
    }
}