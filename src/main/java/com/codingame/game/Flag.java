package com.codingame.game;

public class Flag {
    Coord pos;
    private Minion carrier;

    void setPos(Coord pos) { this.pos = pos; }
    void setCarrier(Minion minion) { this.carrier = minion; }

    public Coord getPos() { return pos; }
    public boolean isCaptured() { return carrier != null; }
    Minion getCarrier() { return carrier; }

    public void drop() {
        this.carrier = null;
    }
}
