package com.codingame.game;

import com.codingame.game.grid.Grid;
import com.codingame.game.grid.TetrisBasedMapGenerator;
import com.google.inject.Singleton;

import java.util.ArrayList;


@Singleton
public class Maze {

    private int[][] grid;
    private int row, col;
    private ArrayList<Coin> availableCoins;

    public int[][] getGrid() {
        return grid;
    }
    int getRow() { return row; }
    int getCol() {return col; }

    void init() {
        row = RandomUtil.randomInt(Config.MIN_MAZE_ROW, Config.MAX_MAZE_ROW);
        col = RandomUtil.randomOddInt(Config.MIN_MAZE_COL, Config.MAX_MAZE_COl);
        grid = new int[row][col];
        Grid gridObj = new Grid(col, row);
        TetrisBasedMapGenerator generator = new TetrisBasedMapGenerator();
        generator.init();
        generator.generateWithHorizontalSymmetry(gridObj, RandomUtil.random);

        System.out.println("Rows = " + row);
        System.out.println("Cols = " +  col);

        for(int i = 0; i < row; i++) {
            for(int j = 0 ; j < col ; j++) {

                grid[i][j] = gridObj.get(j, i).isWall() ? 1 : 0;
                if(i == 0 || j == 0 || i == row-1 || j == col-1) grid[i][j] = 1;
            }
        }

        availableCoins = new ArrayList<>();
    }

    public ArrayList<Coin> getAvailableCoins() {
        return availableCoins;
    }
}
