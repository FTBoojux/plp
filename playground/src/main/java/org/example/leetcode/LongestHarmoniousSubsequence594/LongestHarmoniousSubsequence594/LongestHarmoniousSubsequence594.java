package org.example.leetcode.LongestHarmoniousSubsequence594.LongestHarmoniousSubsequence594;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findLHS(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int ans = 0;
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            int diff = Math.max(map.getOrDefault(num - 1, 0), map.getOrDefault(num + 1, 0));
            ans = Math.max(ans, diff + map.get(num));
        }
        return ans;

    }
}

public class LongestHarmoniousSubsequence594 {
    public static void main(String[] args) {

    }
}
