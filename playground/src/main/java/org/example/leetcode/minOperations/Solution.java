package org.example.leetcode.minOperations;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public int minOperations(int[] nums) {
        int ans = 0;
        List<Integer> stk = new ArrayList<>();
        for (int num : nums) {
            while (!stk.isEmpty() && stk.getLast() > num) {
                stk.removeLast();
            }
            if (num == 0) {
                continue;
            }
            if (stk.isEmpty() || stk.getLast() < num) {
                stk.add(num);
                ++ans;
            }
        }
        return ans;
    }
}