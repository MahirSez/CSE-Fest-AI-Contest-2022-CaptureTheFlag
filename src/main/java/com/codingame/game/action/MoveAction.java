package com.codingame.game.action;

import com.codingame.game.Coord;

public class MoveAction implements Action{

    private final Coord destination;
    public MoveAction(Coord pos) {
        this.destination = pos;
    }
    public Coord getDestination() {
        return this.destination;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.MOVE;
    }

}
