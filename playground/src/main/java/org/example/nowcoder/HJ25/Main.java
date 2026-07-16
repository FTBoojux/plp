package org.example.nowcoder.HJ25;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String string = sc.nextLine();
        char[] charArray = string.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char ch : charArray) {
            if(Character.isAlphabetic(ch)) {
                list.add(ch);
            }
        }
        list.sort((ch1, ch2) -> {
            if (Character.toLowerCase(ch1) == Character.toLowerCase(ch2)) {
                return 0;
            } else {
                return Character.compare(Character.toLowerCase(ch1), Character.toLowerCase(ch2));
            }
        });
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (char ch : charArray) {
            if(!Character.isAlphabetic(ch)){
                sb.append(ch);
            }else{
                sb.append(list.get(index++));
            }
        }
        System.out.println(sb);
    }
}
