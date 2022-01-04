package com.codingame.game.action;

import com.codingame.game.*;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUp implements Action {

    int damage;
    int price;
    int damageDistLimit;
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
    public Coord getOrigin() {
        return this.origin;
    }

    public List<Coord> getAffectedCells(Maze maze) {

        ArrayList<Coord> affectedCoords = new ArrayList<>();
        affectedCoords.add(origin);

        int x = origin.getX();
        int y = origin.getY();

        int[] dx = {1 ,-1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for(int k = 0 ; k < 4 ; k++) {
            for(int scale = 1; scale <= this.damageDistLimit; scale++) {
                int xx = x + dx[k] * scale;
                int yy = y + dy[k] * scale;
                if(xx < 0 || xx >= maze.getRow() || yy < 0 || yy >= maze.getCol() || maze.getGrid()[xx][yy] == 1) {
                    break;
                }
                affectedCoords.add(new Coord(xx, yy));
            }
        }
        return affectedCoords;
    }


    @Override
    public ActionType getActionType() {
        return ActionType.POWER_UP;
    }
}
