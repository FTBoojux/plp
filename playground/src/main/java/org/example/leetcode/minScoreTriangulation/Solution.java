package org.example.leetcode.minScoreTriangulation;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    int length;
    int[] values;
    Map<Integer, Integer> memo = new HashMap<>();
    public int minScoreTriangulation(int[] values) {
        this.length = values.length;
        this.values = values;
        return dp(0,length-1);
    }

    private int dp(int i, int j) {
        if (i+2 > j) {
            return 0;
        }
        if (i+2 == j) {
            return values[i] * values[i+1] * values[j];
        }
        int key = i * length + j;
        if (!memo.containsKey(key)) {
            int min = Integer.MAX_VALUE;
            for (int k = i+1; k < j; ++j) {
                min = Math.min(min,values[i] * values[k] * values[j] + dp(i,k) + dp(k,j));
            }
            memo.put(key, min);
        }
        return memo.get(key);
    }
}
