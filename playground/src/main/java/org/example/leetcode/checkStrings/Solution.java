package org.example.leetcode.checkStrings;

class Solution {
    public boolean checkStrings(String s1, String s2) {
        int[] map = new int[26];
        for(int i = 0; i < s1.length(); i+=2) {
            ++map[s1.charAt(i)-'a'];
        }
        for(int i = 0; i < s2.length(); i += 2) {
            --map[s2.charAt(i)-'a'];
            if(map[s2.charAt(i)-'a'] < 0) {
                return false;
            }
        }
        for(int i = 1; i < s1.length(); i+=2) {
            ++map[s1.charAt(i)-'a'];
        }
        for(int i = 1; i < s2.length(); i += 2) {
            --map[s2.charAt(i)-'a'];
            if(map[s2.charAt(i)-'a'] < 0) {
                return false;
            }
        }
        return true;
    }
}