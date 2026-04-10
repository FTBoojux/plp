package org.example.leetcode.minimumDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public int minimumDistance(int[] nums) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (!map.containsKey(num)) {
                map.put(num, new ArrayList<>());
            }
            map.get(num).add(i);
        }
        boolean existed = false;
        int ans = Integer.MAX_VALUE;
        for (List<Integer> value : map.values()) {
            for (int i = 0; i + 2 < value.size(); i++) {
                ans = Math.min(ans, 2 * (value.get(i+2) - value.get(i)));
                existed = true;
            }
        }
        if (existed) {
            return ans;
        } else {
            return -1;
        }
    }
}