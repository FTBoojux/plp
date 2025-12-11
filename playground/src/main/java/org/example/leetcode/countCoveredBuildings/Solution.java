package org.example.leetcode.countCoveredBuildings;

import java.util.Arrays;

class Solution {
    public int countCoveredBuildings(int n, int[][] buildings) {
        int[] northest = new int[n+1];
        int[] sourthest = new int[n+1];
        Arrays.fill(sourthest,n+1);
        int[] westest = new int[n+1];
        Arrays.fill(westest,n+1);
        int[] eastest = new int[n+1];

        for (int[] building : buildings) {
            int x = building[0],
                    y = building[1];
            northest[x] = Math.max(northest[x],y);
            sourthest[x] = Math.min(sourthest[x],y);
            westest[y] = Math.min(westest[y],x);
            eastest[y] = Math.max(eastest[y],x);
        }
        int ans = 0;
        for (int[] building : buildings) {
            int x = building[0],
                    y = building[1];
            if (
                   northest[x] > y
                    && sourthest[x] < y
                    && westest[y] < x
                    && eastest[y] > x
            ) {
                ++ans;
            }
        }
        return ans;
    }
}