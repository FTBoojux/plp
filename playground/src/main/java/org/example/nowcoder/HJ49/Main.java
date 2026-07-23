package org.example.nowcoder.HJ49;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        List<Candidate> list = new ArrayList<>();
        for(int i = 0; i < n; ++i){
            list.add(new Candidate(sc.nextInt(), sc.nextInt()));
        }
        int candidateNumber = m + m/2;
        Collections.sort(list,(a,b)-> {
            if(a.score != b.score){
                return b.score - a.score;
            }else{
                return a.number - b.number;
            }
        });
        int line = list.get(candidateNumber-1).score;
        int total = 0;
        for(int i = 0; i < n; ++i){
            if(list.get(i).score >= line){
                ++total;
            }
        }
        System.out.println(line + " " + total);
        for(int i = 0; i < n; ++i){
            if(list.get(i).score >= line){
                System.out.println(list.get(i).number + " " + list.get(i).score);
            }else{
                break;
            }
        }
    }
    private static class Candidate {
        public int number;
        public int score;
        public int getScore() {
            return score;
        }
        public Candidate(int number, int score){
            this.number = number;
            this.score = score;
        }
    }
}
