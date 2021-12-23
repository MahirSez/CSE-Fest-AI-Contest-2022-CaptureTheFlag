package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Arrays;


@Singleton
public class Maze {


    @Inject private MultiplayerGameManager<Player> gameManager;
    private int[][] grid;
    private int row, col;

    int[][] getGrid() {
        return grid;
    }
    int getRow() { return row; }
    int getCol() {return col; }

    void init() {
        row = RandomUtil.randomInt(Config.MIN_MAZE_ROW, Config.MAX_MAZE_ROW);
        col = RandomUtil.randomInt(Config.MIN_MAZE_COL, Config.MAX_MAZE_COl);
        grid = new int[row][col];

        System.out.println("Rows = " + row);
        System.out.println("Cols = " +  col);
        for(int i = 0; i < row; i++) {
            for(int j = 2 ; j < col - 2 ; j++) {
                grid[i][j] = (RandomUtil.randomInt(0, 2) == 0 ? 1 : 0);
            }
            System.out.println(Arrays.toString(grid[i]));
        }

    }

}
