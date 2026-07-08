package org.example.nowcoder.HJ13;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] s = line.split(" ");
        int size = s.length;
        for(int i = size-1; i >= 0; --i) {
            System.out.print(s[i] + " ");
        }
    }
}
