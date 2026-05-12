package org.example.leetcode.minimumEffort;

import java.util.Arrays;

class Solution {
    public int minimumEffort(int[][] tasks) {
        Arrays.sort(tasks, (a,b) -> {
            int a_diff = a[1] - a[0];
            int b_diff = b[1] - b[0];
            if(a_diff != b_diff){
                return a_diff - b_diff;
            }else{
                return a[0] - b[0];
            }
        });
        int cost = 0;
        for(int[] task : tasks){
            cost += task[0];
            if(cost < task[1]){
                cost = task[1];
            }
            task[0] = cost;
        }
        return cost;
    }
}