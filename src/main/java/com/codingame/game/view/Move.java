package com.codingame.game.view;

import com.codingame.game.Coord;
import com.codingame.game.Minion;

public class Move {
    Coord from, to;
    Minion minion;

    public Move(Minion minion, Coord from, Coord to) {
        this.minion = minion;
        this.from = from;
        this.to = to;

    }
}
