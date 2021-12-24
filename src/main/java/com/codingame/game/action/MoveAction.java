package com.codingame.game.action;

import com.codingame.game.Coord;

public class MoveAction implements Action{

    public MoveAction(Coord pos) {

    }
    @Override
    public ActionType getActionType() {
        return ActionType.MOVE;
    }
}
