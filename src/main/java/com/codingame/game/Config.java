package com.codingame.game;

public class Config {

    static final int MINION_TOTAL_HEALTH = 100;

    static final int LEFT_PLAYER_COLOR = 0x0000ff;
    static final int RIGHT_PLAYER_COLOR = 0xff0000;
    static final String LEFT_PLAYER_COLOR_NAME = "blue";
    static final String RIGHT_PLAYER_COLOR_NAME = "red";
    static final int WALL_COLOR = 0x964B00;


    static final int MIN_MINIONS = 3;
    static final int MAX_MINIONS = 5;

    static final int MIN_MAZE_ROW = 12;
    static final int MAX_MAZE_ROW = 15;

    static final int MIN_MAZE_COL = 30;
    static final int MAX_MAZE_COl = 35;

    static final int MAZE_UPPER_OFFSET = 200;

    public static final Coord[] ADJACENCY = {
            new Coord(-1, 0),
            new Coord(1, 0),
            new Coord(0, -1),
            new Coord(0, 1)
    };
}
