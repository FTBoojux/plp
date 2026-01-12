package org.example.leetcode.minTimeToVisitAllPoints;

class Solution {
    public int minTimeToVisitAllPoints(int[][] points) {
        int ans = 0;
        int length = points.length;
        for (int i = 1; i < length; i++) {
           int[] currentPoint = points[i],
                previousPoint = points[i-1];
           int distance = Math.max(
                   Math.abs(currentPoint[0] - previousPoint[0]),
                   Math.abs(currentPoint[1] - previousPoint[1])
           );
           ans += distance;
        }
        return ans;
    }
}