package com.codingame.game;

public class Flag {
    Coord pos;
    private boolean captured;
    Flag() {
        this.captured = false;
    }
    void setPos(Coord pos) { this.pos = pos; }
    Coord getPos() { return pos; }

}
