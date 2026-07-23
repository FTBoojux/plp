package org.example.nowcoder.HJ31;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(char ch : string.toCharArray()){
            if(Character.isAlphabetic(ch)){
                sb.append(ch);
            }else{
                list.add(sb.toString());
                sb.delete(0,sb.length());
            }
        }
        if(!sb.isEmpty()){
            list.add(sb.toString());
        }
        Collections.reverse(list);
        String ans = list.stream().collect(Collectors.joining(" "));
        System.out.println(ans);
    }
}
