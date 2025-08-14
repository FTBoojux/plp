package org.example.leetcode.largest3SameDigitNumberInString;

public class Solution {
    public String largestGoodInteger(String num) {
        String ans = "";
        for(int left = 0, right = 0; right < num.length(); ++right){
            if (num.charAt(right) == num.charAt(left)){
                if (right - left >= 2){
                    String cur = num.substring(left, right);
                    if(cur.compareTo(ans) > 0){
                        ans = cur;
                    }
                    while (num.charAt(right) == num.charAt(left)){
                        ++right;
                    }
                    --right;
                }
            }else{
                left = right;
            }
        }
        return ans;
    }
}