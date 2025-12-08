package org.example.leetcode.countCollisions;

class Solution {
    public int countCollisions(String directions) {
        int length = directions.length();
        int[] rightObstacle = new int[length];
        int[] leftObstacle = new int[length];
        int index = -1;
        for (int i = 0; i < length; ++ i) {
            if (directions.charAt(i) != 'L') {
                index = i;
            }
            rightObstacle[i] = index;
        }
        index = length + 1;
        for (int i = length-1; i >= 0; --i) {
            if (directions.charAt(i) != 'R') {
                index = i;
            }
            leftObstacle[i] = index;
        }
        int ans = 0;
        for (int i = 0; i < length; ++i) {
            if (directions.charAt(i) == 'S'){
                continue;
            }
            if (directions.charAt(i) == 'L') {
                if (leftObstacle[i] < length+1) {
                    ++ans;
                    continue;
                }
            }
            if (directions.charAt(i) == 'R') {
                if (rightObstacle[i] > -1) {
                    ++ans;
                }
            }
        }
        return ans;
    }
}