package org.example.leetcode.DeleteCharacterstoMakeFancyString;

public class Solution {
    public String makeFancyString(String s) {
        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if(i > 0 && s.charAt(i) == s.charAt(i-1)){
                ++cnt;
            }else{
                cnt = 1;
            }
            if(cnt > 2){
                continue;
            }else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
}
