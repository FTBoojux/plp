package org.example.nowcoder.HJ5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine();
        str = str.substring(2);
        int length = str.length();
        int ans = 0;
        for(int i = 0; i < length; i++){
            int num = parseNum(str.charAt(i));
            ans += num * Math.pow(16, length - i - 1);
        }
        System.out.println(ans);
    }
    public static int parseNum(char ch){
        if(Character.isDigit(ch)){
            return ch-'0';
        }else{
            return ch-'A'+10;
        }
    }
}