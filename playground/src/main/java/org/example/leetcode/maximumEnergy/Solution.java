package org.example.leetcode.maximumEnergy;

public class Solution {
    public int maximumEnergy(int[] energy, int k) {
        int len = energy.length;
        int ans = Integer.MIN_VALUE;
        for (int i = k; i < len; ++i) {
            energy[i] += Math.max(0,energy[i-k]);
        }
        for (int i = 1; i <=k; ++i) {
            ans = Math.max(ans,energy[len-i]);
        }
        return ans;
    }
}