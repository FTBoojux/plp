package org.example.nowcoder.HJ43;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int[][] MOVE = {{1,0},{-1,0},{0,1},{0,-1}};
    private static List<int[]> ans = new ArrayList<>();
    private static boolean[][] visited;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();
        int w = in.nextInt();
        int[][] a = new int[h][w];
        visited = new boolean[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                a[i][j] = in.nextInt();
            }
        }
        bfs(a,h,w,0,0, new ArrayList<>());
        for (int[] an : ans) {
            System.out.printf("(%d,%d)\n",an[0], an[1]);
        }
    }
    private static void bfs(int[][] a, int h, int w, int x, int y, List<int[]> path) {
        path.add(new int[]{x,y});
        visited[x][y] = true;
        if(x + 1 == h && y + 1 == w){
            ans = path;
            return;
        }
        for (int[] move : MOVE) {
            int nx = x + move[0];
            int ny = y + move[1];
            if(nx >= 0 && nx < h && ny >=0 && ny < w && a[nx][ny] != 1 && !visited[nx][ny]){
                bfs(a, h, w, nx, ny, new ArrayList<>(path));
            }
        }
    }
}
