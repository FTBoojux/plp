package org.example.leetcode.maxOperations;

public class Solution {
    public int maxOperations(String s) {
        int ans = 0,
                one = 0;
        for(int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '1') {
                ++one;
            } else {
                while (i + 1 < s.length() && s.charAt(i+1) == '0') {
                    ++i;
                }
                ans += one;
            }
        }
        return ans;
    }
}