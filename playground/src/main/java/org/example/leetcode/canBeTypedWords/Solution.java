package org.example.leetcode.canBeTypedWords;

import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int could = solution.canBeTypedWords("leet code", "e");
        System.out.println(could);
    }
    public int canBeTypedWords(String text, String brokenLetters) {
        Set<Character> set = new HashSet<>();
        for (char ch : brokenLetters.toCharArray()) {
            set.add(ch);
        }
        int ans = 0,cnt = 0;
        for (char ch : text.toCharArray()) {
            if (ch == ' '){
                if (cnt > 0) {
                    ++ans;
                    cnt = 0;
                }
            }else{
                if (set.contains(ch)){
                    ++cnt;
                }
            }
        }
        if (cnt > 0) {
            ++ans;
        }
        return ans;
    }
}