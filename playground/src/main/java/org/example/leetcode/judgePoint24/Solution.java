package org.example.leetcode.judgePoint24;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private static final double ZERO = 1e-9;
    public boolean judgePoint24(int[] cards) {
        List<Double> list = new ArrayList<>();
        for (int card : cards) {
            list.add((double) card);
        }
        return judgePoint24(list);
    }

    private boolean judgePoint24(List<Double> cards) {
        boolean ans = false;
        for (int i = 0; i < cards.size(); ++i) {
            for (int j = i+1; j < cards.size(); ++j){
                List<Double> list = new ArrayList<>(cards);
                list.remove(j);
                list.remove(i);

                Double add = cards.get(i) + cards.get(j);
                Double minus1 = cards.get(i) - cards.get(j);
                Double minus2 = cards.get(j) - cards.get(i);
                Double multi = cards.get(i) * cards.get(j);
                Double divide1 = 0D;
                if (cards.get(j) != 0){
                    divide1 = cards.get(i) / cards.get(j);
                }
                Double divide2 = 0D;
                if (cards.get(i) != 0){
                    divide2 = cards.get(j) / cards.get(i);
                }
                if (list.isEmpty()){
                    return Math.abs(add-24) < ZERO || Math.abs(minus1-24) < ZERO || Math.abs(minus2-24) < ZERO || Math.abs(multi-24) < ZERO || Math.abs(divide1-24) < ZERO || Math.abs(divide2-24) < ZERO;
                }else {
                    List<Double> list1 = new ArrayList<>(list),
                            list2 = new ArrayList<>(list),
                            list3 = new ArrayList<>(list),
                            list4 = new ArrayList<>(list),
                            list5 = new ArrayList<>(list),
                            list6 = new ArrayList<>(list);
                    list1.add(add);
                    list2.add(minus1);
                    list3.add(minus2);
                    list4.add(multi);
                    list5.add(divide1);
                    list6.add(divide2);
                    ans = ans || judgePoint24(list1) || judgePoint24(list2) || judgePoint24(list3) || judgePoint24(list4) || judgePoint24(list5) || judgePoint24(list6);
                }
            }
        }
        return ans;
    }
}