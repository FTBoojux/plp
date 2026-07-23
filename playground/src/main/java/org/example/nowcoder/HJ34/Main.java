package org.example.nowcoder.HJ34;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        char[] charArray = s.toCharArray();
        Arrays.sort(charArray);
        String ans = new String(charArray);
        System.out.println(ans);
    }
}
