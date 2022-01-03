package com.codingame.game.action;

import com.codingame.game.Config;
import com.codingame.game.Coord;
import com.codingame.game.Game;
import com.codingame.game.Minion;

import java.util.ArrayList;
import java.util.List;

public class FreezePower extends PowerUp{

    public FreezePower(Coord origin, Minion powerUpUser) {
        super(origin, powerUpUser);
        this.damage = Config.FREEZE_TIMEOUT;
        this.price = Config.FREEZE_PRICE;
    }

    @Override
    public List<Minion> damageMinions(Game game) {
        List<Minion>frozenMinions = new ArrayList<>();
        System.out.println("Freezer " + this.powerUpUser.getOwner().getColor() + " " + this.powerUpUser.getID());
        for(Minion minion: game.getAliveMinions()) {
            if(minion != this.powerUpUser && game.isVisible(minion.getPos(), this.origin) ) {
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
