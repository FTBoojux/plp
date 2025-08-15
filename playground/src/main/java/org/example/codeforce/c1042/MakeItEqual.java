package org.example.codeforce.c1042;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MakeItEqual {
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        int t = in.nextInt();
        MakeItEqual solution = new MakeItEqual();
        while (t-- > 0) {
            solution.solve(in);
        }
    }

    public void solve(Scanner in) {
        int n = in.nextInt(), k = in.nextInt();
        int[] s = new int[n],
                t = new int[n];
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < n; ++i){
            int num = in.nextInt();
            s[i] = num > 0 ? Math.min(num%k,k-num%k) : 0;
            map.put(s[i],map.getOrDefault(s[i],0)+1);
        }
        for(int i = 0; i < n; ++i){
            int num = in.nextInt();
            t[i] = num > 0 ? Math.min(num%k,k-num%k) : 0;
            if (!map.containsKey(t[i])){
                continue;
            }
            map.put(t[i],map.get(t[i])-1);
            if (map.get(t[i]) == 0){
                map.remove(t[i]);
            }
        }
        if (map.isEmpty()){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }
    }
}
