package com.codingame.game.action;

import com.codingame.game.Config;
import com.codingame.game.Coord;
import com.codingame.game.Game;
import com.codingame.game.Minion;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class FirePower extends PowerUp {

    public FirePower(Coord origin, Minion minion) {
        super(origin, minion);
        this.damage = Config.FIRE_DAMAGE;
        this.price = Config.FIRE_PRICE;
    }


    @Override
    public List<Minion> damageMinions(Game game) {
        List<Minion>damagedMinions = new ArrayList<>();
        System.out.println("Shooting " + this.powerUpUser.getOwner().getColor() + " " + this.powerUpUser.getID());
        for(Minion minion: game.getAliveMinions()) {
            if(minion != this.powerUpUser && game.isVisible(minion.getPos(), this.origin) ) {
                System.out.println("\tDealing Damage " + minion.getOwner().getColor() + " " + minion.getID());
                minion.dealDamage(this.damage);
                damagedMinions.add(minion);
            }
        }
        return damagedMinions;
    }

    @Override
    public PowerUpType getPowerType() {
        return PowerUpType.FIRE ;
    }
}
