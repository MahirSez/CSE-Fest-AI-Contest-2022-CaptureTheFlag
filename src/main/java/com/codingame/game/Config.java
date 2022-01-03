package com.codingame.game;

public class Config {

    public static final int MINION_TOTAL_HEALTH = 100;

    public static final int RIGHT_PLAYER_COLOR = 0x0000ff;
    public static final int LEFT_PLAYER_COLOR = 0xff0000;
    public static final String LEFT_PLAYER_COLOR_NAME = "red";
    public static final String RIGHT_PLAYER_COLOR_NAME = "blue";


    public static final int MIN_MINIONS = 3;
    public static final int MAX_MINIONS = 3;

    public static final int MIN_MAZE_ROW = 17;
    public static final int MAX_MAZE_ROW = 20;

    public static final int MIN_MAZE_COL = 33;
    public static final int MAX_MAZE_COl = 35;

    public static final int MAZE_UPPER_OFFSET = 200;


    public static final int FIRE_DAMAGE = 60;
    public static final int FREEZE_TIMEOUT = 10;


    public static final int FIRE_PRICE = 20;
    public static final int FREEZE_PRICE = 10;

    public static final Coord[] ADJACENCY = {
            new Coord(-1, 0),
            new Coord(1, 0),
            new Coord(0, -1),
            new Coord(0, 1)
    };

    public static final int INF = 10000000;
    public static final int FRAME_DURATION = 300;
}
