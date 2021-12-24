package com.codingame.game.action;

public interface Action {
    Action NO_ACTION = new Action() {

        @Override
        public ActionType getActionType() {
            return ActionType.WAIT;
        }
    };
    ActionType getActionType();
}
