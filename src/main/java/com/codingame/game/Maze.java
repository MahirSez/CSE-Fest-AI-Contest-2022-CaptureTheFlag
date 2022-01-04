package com.codingame.game;

import com.codingame.game.grid.Grid;
import com.codingame.game.grid.TetrisBasedMapGenerator;
import com.google.inject.Singleton;

import java.util.ArrayList;


@Singleton
public class Maze {

    private int[][] grid;
    private int row, col;


    private boolean[][][] rowVisibility, colVisibility;

    public int[][] getGrid() {
        return grid;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private void setRowVisibility() {
        rowVisibility = new boolean[row][col][col];
        for (int r = 0; r < row; r++) {
            for (int i = 0; i < col; i++) {
                boolean visible = true;
                for (int j = i; j >= 0; j--) {
                    visible = visible && (grid[r][j] == 0);
                    rowVisibility[r][i][j] = visible;
                }
                visible = true;
                for (int j = i; j < col; j++) {
                    visible = visible && (grid[r][j] == 0);
                    rowVisibility[r][i][j] = visible;
                }
            }
        }
    }

    private void setColVisibility() {
        colVisibility = new boolean[col][row][row];
        for (int c = 0; c < col; c++) {
            for (int i = 0; i < row; i++) {
                boolean visible = true;
                for (int j = i; j >= 0; j--) {
                    visible = visible && (grid[j][c] == 0);
                    colVisibility[c][i][j] = visible;
                }
                visible = true;
                for (int j = i; j < row; j++) {
                    visible = visible && (grid[j][c] == 0);
                    colVisibility[c][i][j] = visible;
                }
            }
        }
    }

    void init() {
        row = RandomUtil.randomInt(Config.MIN_MAZE_ROW, Config.MAX_MAZE_ROW);
        col = RandomUtil.randomOddInt(Config.MIN_MAZE_COL, Config.MAX_MAZE_COl);
        grid = new int[row][col];
        Grid gridObj = new Grid(col, row);
        TetrisBasedMapGenerator generator = new TetrisBasedMapGenerator();
        generator.init();
        generator.generateWithHorizontalSymmetry(gridObj, RandomUtil.random);

        System.out.println("Rows = " + row);
        System.out.println("Cols = " + col);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                grid[i][j] = gridObj.get(j, i).isWall() ? 1 : 0;
                if (i == 0 || j == 0 || i == row - 1 || j == col - 1) grid[i][j] = 1;
            }
        }


      
        setRowVisibility();
        setColVisibility();
    }

    public boolean isVisible(Coord pos1, Coord pos2) {
        int x1 = pos1.getX(), y1 = pos1.getY();
        int x2 = pos2.getX(), y2 = pos2.getY();
        if (x1 == x2) {
            return rowVisibility[x1][y1][y2];
        }
        if (y1 == y2) {
            return colVisibility[y1][x1][x2];
        }
        return false;
    }

    public boolean isVisible(Coord pos1, Coord pos2, int limit) {
        int x1 = pos1.getX(), y1 = pos1.getY();
        int x2 = pos2.getX(), y2 = pos2.getY();
        if (x1 == x2) {
            return rowVisibility[x1][y1][y2] && Math.abs(y1 - y2) <= limit;
        }
        if (y1 == y2) {
            return colVisibility[y1][x1][x2] && Math.abs(x1 - x2) <= limit;
        }
        return false;
    }
}
