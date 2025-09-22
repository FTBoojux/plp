package org.example.leetcode.maxFrequencyElements;

public class Solution {
    public int maxFrequencyElements(int[] nums) {
        int[] map = new int[101];
        int max_frequent = 0;
        int cnt = 0;
        for (int num : nums) {
            ++map[num];
            if (map[num] > max_frequent) {
                max_frequent = map[num];
                cnt = 1;
            } else if (map[num] == max_frequent) {
                ++cnt;
            }
        }
        return cnt * max_frequent;
    }
}