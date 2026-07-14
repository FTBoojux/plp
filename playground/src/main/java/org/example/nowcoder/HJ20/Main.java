package org.example.nowcoder.HJ20;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            String password = in.nextLine();
            boolean ans = solve(password);
            System.out.println(ans ? "OK" : "NG");
        }
    }

    private static boolean solve(String password) {
        if(password.length() < 8){
            return false;
        }
        Set<String> set = new HashSet<>();
        int cnt = 0;
        for(int i = 0; i < password.length(); ++i){
            char cur = password.charAt(i);
            if(cur < 33 || cur > 126){
                return false;
            }
            if(Character.isUpperCase(cur)){
                cnt |= 1;
            }else if(Character.isLowerCase(cur)){
                cnt |= 2;
            }else if(Character.isDigit(cur)){
                cnt |= 4;
            } else{
                cnt |= 8;
            }
            if(i + 3 < password.length()){
                String substring = password.substring(i, i + 3);
                if(set.contains(substring)){
                    return false;
                }else{
                    set.add(substring);
                }
            }
        }
        return Integer.bitCount(cnt) >= 3;
    }
}
