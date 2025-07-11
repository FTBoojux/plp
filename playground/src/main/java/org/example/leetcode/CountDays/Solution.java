package org.example.leetcode.CountDays;

import java.util.Arrays;

class Solution {
    public int countDays(int days, int[][] meetings) {
        int ans = 0;
        Arrays.sort(meetings, (a,b)->a[0]==b[0] ? b[1] - a[1] : a[0] - b[0]);
        int day = 0;
        for (int[] meeting : meetings) {
            if(meeting[0] > day){
                ans += meeting[0] - day-1;
            }
            if(meeting[1] > day){
                day = meeting[1];
            }
        }
        ans += days > day ? days - day : 0;
        return ans;
    }
}