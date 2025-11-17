package org.example.leetcode.kLengthApart;

public class Solution {
    public boolean kLengthApart(int[] nums, int k) {
        int distance = k;
        for (int num : nums ) {
            if (num == 0) {
                ++distance;
            } else if (num == 1) {
                if (distance < k) {
                    return false;
                }
                distance = 0;
            }
        }
        return true;
    }
}