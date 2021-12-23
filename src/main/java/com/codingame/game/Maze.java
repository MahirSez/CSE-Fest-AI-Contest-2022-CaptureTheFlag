package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


@Singleton
public class Maze {


    @Inject private MultiplayerGameManager<Player> gameManager;
    private int[][] grid;
    private int ro, col;

    int[][] getGrid() {
        return grid;
    }
    int getRo() { return ro; }
    int getCol() {return col; }

    void init() {
        ro = RandomUtil.randomInt(Config.MIN_MAZE_ROW, Config.MAX_MAZE_ROW);
        col = RandomUtil.randomInt(Config.MIN_MAZE_COL, Config.MAX_MAZE_COl);
        grid = new int[ro][col];

        System.out.println("Rows = " + ro);
        System.out.println("Cols = " +  col);
        for(int i = 0 ; i < ro ; i++) {
            for(int j = 2 ; j < col - 2 ; j++) {
                grid[i][j] = (RandomUtil.randomInt(0, 2) == 0 ? 1 : 0);
            }
            System.out.println(Arrays.toString(grid[i]));
        }

    }

}
