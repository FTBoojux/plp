package org.example.leetcode.minimumBoxes;

import java.util.Arrays;

class Solution {
    public int minimumBoxes(int[] apple, int[] capacity) {
        Arrays.sort(capacity);
        int sum = 0;
        for (int a : apple) {
            sum += a;
        }
        int ans = 0;
        for(int i = capacity.length-1;i>=0;--i){
            if (sum <= 0) {
                break;
            }
            ++ans;
            sum -= capacity[i];
        }
        return ans;
    }
}