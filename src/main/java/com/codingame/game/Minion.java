package com.codingame.game;

public class Minion {

    int minionID;
    Player owner;
    int health;
    Coord pos;

    Minion(int minionID, Player owner) {
        this.minionID = minionID;
        this.owner = owner;
        health = Config.MINION_TOTAL_HEALTH;
    }
    void setPosition(Coord pos) {
        this.pos = pos;
    }
    Coord getPos() { return this.pos; }
}
