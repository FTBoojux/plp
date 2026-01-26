package org.example.leetcode.minimumAbsDifference;

import java.util.*;

class Solution {
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Map<Integer, List<List<Integer>>> map = new HashMap<>();
        Arrays.sort(arr);
        int min = Integer.MAX_VALUE;
        for(int i = 1; i < arr.length; ++i) {
            int diff = arr[i] - arr[i-1];
            List<List<Integer>> lists = map.computeIfAbsent(diff, _ -> new ArrayList<>());
            lists.add(Arrays.asList(arr[i-1],arr[i]));
            min = Math.min(min, diff);
        }
        return map.get(min);
    }
}