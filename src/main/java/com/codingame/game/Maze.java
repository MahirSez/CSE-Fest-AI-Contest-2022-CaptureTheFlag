package com.codingame.game;

import com.codingame.game.grid.Grid;
import com.codingame.game.grid.TetrisBasedMapGenerator;
import com.google.inject.Singleton;


@Singleton
public class Maze {

    private int[][] grid;
    private int row, col;

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
        int flagRow = row / 2, leftFlagCol = 0, rightFlagCol = col - 1;
//        for (int i = 0; i < row; i++) {
//            if (i != flagRow) {
//                grid[i][leftFlagCol] = grid[i][rightFlagCol] = 1;
//            }
//        }
        for(int i = 0; i < row; i++) {
            for(int j = 0 ; j < col ; j++) {
                grid[i][j] = gridObj.get(j, i).isWall() ? 1 : 0;
            }
        }
    }

}
