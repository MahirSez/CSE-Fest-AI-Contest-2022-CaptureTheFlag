package com.codingame.game.action;

import com.codingame.game.*;

import java.util.ArrayList;
import java.util.List;

public class MinePower extends PowerUp{

//    private final int damageDistance;

    public MinePower(Coord origin, Minion powerUpUser) {
        super(origin, powerUpUser);
        this.damage = Config.MINE_DAMAGE;
        this.price = Config.MINE_PRICE;
        this.damageDistLimit = Config.MINE_RANGE;
    }

    @Override
    public List<Minion> damageMinions(Game game, Maze maze) {
        List<Minion>damagedMinions = new ArrayList<>();
        System.out.println("Mine Detonated");
        for(Minion minion: game.getAliveMinions()) {
            if(maze.isVisible(minion.getPos(), this.origin, damageDistLimit)) {
                System.out.println("\tDealing Damage " + minion.getOwner().getColor() + " " + minion.getID());
                minion.dealDamage(this.damage);
                damagedMinions.add(minion);
            }
        }
        return damagedMinions;
    }

    @Override
    public PowerUpType getPowerType() {
        return PowerUpType.MINE;
    }

    public boolean placeable(Maze maze) {
        int x = origin.getX();
        int y = origin.getY();
        return origin.manhattanTo(powerUpUser.getPos()) == 1 && maze.getGrid()[x][y] == 0;
    }
}
