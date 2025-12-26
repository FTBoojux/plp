package org.example.leetcode.bestClosingTime;

class Solution {
    public int bestClosingTime(String customers) {
        int length = customers.length();
        int sum = 0;
        char[] customersArray = customers.toCharArray();
        for (char c : customersArray) {
            if (c == 'Y') {
                sum++;
            }
        }
        int ans = 0,minPenal = length;
        for (int i = 0; i < length;++i) {
            if (sum < minPenal) {
                ans = i;
                minPenal = sum;
            }
            if (customersArray[i] == 'Y') {
                --sum;
            } else {
                ++sum;
            }
        }
        if (sum < minPenal) {
            ans = length;
        }
        return ans;
    }
}