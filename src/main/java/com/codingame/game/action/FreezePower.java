package com.codingame.game.action;

import com.codingame.game.*;

import java.util.ArrayList;
import java.util.List;

public class FreezePower extends PowerUp{

    public FreezePower(Coord origin, Minion powerUpUser) {
        super(origin, powerUpUser);
        this.damage = Config.FREEZE_TIMEOUT;
        this.price = Config.FREEZE_PRICE;
        this.damageDistLimit = Config.INF;
    }

    @Override
    public List<Minion> damageMinions(Game game, Maze maze) {
        List<Minion>frozenMinions = new ArrayList<>();
        System.out.println("Freezer " + this.powerUpUser.getOwner().getColor() + " " + this.powerUpUser.getID());
        for(Minion minion: game.getAliveMinions()) {
            if(minion != this.powerUpUser && maze.isVisible(minion.getPos(), this.origin) ) {
                System.out.println("\tGetting frozen " + minion.getOwner().getColor() + " " + minion.getID());
                minion.addTimeOut(this.damage);
                frozenMinions.add(minion);
            }
        }
        return frozenMinions;
    }

    @Override
    public PowerUpType getPowerType() {
        return PowerUpType.FREEZE;
    }
}
