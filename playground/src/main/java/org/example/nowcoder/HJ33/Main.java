package org.example.nowcoder.HJ33;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String ipAddress = sc.nextLine();
        String number = sc.next();
        System.out.println(toNum(ipAddress));
        System.out.println(toIpaddress(number));
    }
    private static String toNum(String ipAddress){
        String[] raw = ipAddress.split("\\.");
        long ans = 0;
        for (String string : raw) {
            long num = Long.parseLong(string);
            ans = (ans << 8) + num;
        }
        return String.valueOf(ans);
    }
    private static String toIpaddress(String number) {
        long num = Long.parseLong(number);
        StringBuilder sb = new StringBuilder();
        long mask = (long) Math.pow(2,8) - 1;
        mask <<= 24;
        int offset = 24;
        while(mask > 0){
            sb.append((num & mask) >> offset);
            offset -=8;
            mask >>= 8;
            if(mask > 0){
                sb.append(".");
            }
        }
        return sb.toString();
    }
}
