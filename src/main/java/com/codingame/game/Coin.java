package com.codingame.game;

public class Coin {
    private final Coord position;
    private final int value;

    public Coin(Coord position, int value) {
        this.position = position;
        this.value = value;
    }

    public Coord getPosition() {
        return position;
    }

    public int getValue() {
        return value;
    }
}
