package org.example.leetcode.getCommon;

import java.util.HashSet;
import java.util.Set;

class Solution {
    public int getCommon(int[] nums1, int[] nums2) {
        int length1 = nums1.length;
        int length2 = nums2.length;
        if(nums1[length1 - 1] < nums2[0]
            ||  nums2[length2 - 1] < nums1[0]
        ) {
            return -1;
        }
        Set<Integer> set = new HashSet<>();
        for (int num : nums1) {
            set.add(num);
        }
        for (int num : nums2) {
            if (set.contains(num)) {
                return num;
            }
        }
        return -1;
    }
}