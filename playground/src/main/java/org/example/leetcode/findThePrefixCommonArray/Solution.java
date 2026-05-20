package org.example.leetcode.findThePrefixCommonArray;

class Solution {
    public int[] findThePrefixCommonArray(int[] A, int[] B) {
        int size = A.length;
        int[] count = new int[size];
        int[] ans = new int[size];
        int total = 0;
        for(int i = 0; i < size; i++){
            int a = A[i]-1;
            int b = B[i]-1;
            count[a]++;
            if(count[a] == 2){
                ++total;
            }
            count[b]++;
            if(count[b] == 2){
                ++total;
            }
            ans[i] = total;
        }
        return ans;
    }
}