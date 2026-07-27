package org.example.nowcoder.HJ54;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] split = line.split(",");
        long last = Long.parseLong(split[3]);
        long second = Long.parseLong(split[1]);
        System.out.println(last-second-1);
    }
}
