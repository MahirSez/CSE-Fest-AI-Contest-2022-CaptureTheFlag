package com.codingame.game.action;

import com.codingame.game.Coord;
import com.codingame.game.Game;
import com.codingame.game.Minion;
import com.codingame.game.Player;
import com.google.inject.Inject;

import java.util.List;

public abstract class PowerUp implements Action {

    int damage;
    int price;
    Coord origin;
    Minion powerUpUser;
    @Inject Game game;

    PowerUp(Coord origin, Minion powerUpUser) {
        this.origin = origin;
        this.powerUpUser = powerUpUser;
    }

    public abstract List<Minion> damageMinions();
    public abstract PowerUpType getPowerType();

    /**
     *
     * Todo: uncomment the line after credit has been added
     */
    public boolean canBuy(Player player) {
        return true;
//        return player.getScore() >= this.price;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.POWER_UP;
    }
}
