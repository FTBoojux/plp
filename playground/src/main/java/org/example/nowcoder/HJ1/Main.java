package org.example.nowcoder.HJ1;

import org.example.codeforce.Solution;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            String[] s = str.split(" ");
            int length = s.length;
            System.out.println(s[length - 1].length());
        }
    }
}