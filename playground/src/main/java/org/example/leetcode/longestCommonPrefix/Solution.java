package org.example.leetcode.longestCommonPrefix;

import java.util.HashSet;
import java.util.Set;

class Solution {
    public int longestCommonPrefix(int[] arr1, int[] arr2) {
        Set<Integer> arr1_prefix = new HashSet<>();
        for (int num : arr1) {
            while(num > 0) {
                arr1_prefix.add(num);
                num /= 10;
            }
        }
        int ans = 0;
        for (int num : arr2) {
            while(num > 0) {
                if(arr1_prefix.contains(num)) {
                    int digits = String.valueOf(num).length();
                    ans = Math.max(ans, digits);
                }
                num /= 10;
            }
        }
        return ans;
    }
}
