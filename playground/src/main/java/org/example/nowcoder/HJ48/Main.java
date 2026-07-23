package org.example.nowcoder.HJ48;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Integer> list = new ArrayList<>();
        int h = in.nextInt();
        list.add(h);
        for(int i = n-1; i > 0; --i) {
            int a = in.nextInt();
            int b = in.nextInt();
            insert(list, a, b);
        }
        int k = in.nextInt();
        remove(list, k);
        for (Integer i : list) {
            System.out.print(i + " ");
        }
    }
    public static void remove(List<Integer> list, int k) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == k){
                index = i;
                break;
            }
        }
        if(index != -1){
            list.remove(index);
        }
    }
    public static void insert(List<Integer> list, int a, int b) {
        int index = -1;
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i) == b){
                index = i;
                break;
            }
        }
        if(index != -1){
            list.add(index+1, a);
        }
    }
}
