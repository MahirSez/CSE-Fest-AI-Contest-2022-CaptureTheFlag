package com.codingame.game.action;

import com.codingame.game.*;

import java.util.List;

public abstract class PowerUp implements Action {

    int damage;
    int price;
    Coord origin;
    Minion powerUpUser;



    PowerUp(Coord origin, Minion powerUpUser) {
        this.origin = origin;
        this.powerUpUser = powerUpUser;
    }

    public abstract List<Minion> damageMinions(Game game, Maze maze);
    public abstract PowerUpType getPowerType();

    public boolean canBuy(Player player) {
        return player.getCurrentCredit() >= this.price;
    }
    public int getPrice() {
        return this.price;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.POWER_UP;
    }
}
