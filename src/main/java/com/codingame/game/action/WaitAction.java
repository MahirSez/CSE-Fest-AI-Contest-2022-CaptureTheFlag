package com.codingame.game.action;

public class WaitAction implements Action{
    @Override
    public ActionType getActionType() {
        return ActionType.WAIT;
    }
}
