package org.example.nowcoder.HJ14;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = sc.next();
        }
        Arrays.sort(strings);
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
