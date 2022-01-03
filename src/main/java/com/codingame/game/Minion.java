package com.codingame.game;

import com.codingame.game.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Minion {

    int minionID;
    Player owner;
    int health, timeOut;
    Coord pos;
    List<String> gameSummary;
    Action intendedAction;
    List<Coord>pathToDest;

    Minion(int minionID, Player owner) {
        this.minionID = minionID;
        this.owner = owner;
        this.health = Config.MINION_TOTAL_HEALTH;
        this.intendedAction = Action.NO_ACTION;
        this.gameSummary = new ArrayList<>();
        this.pathToDest = new ArrayList<>();
    }

    void setPos(Coord pos) { this.pos = pos; }
    void addSummary(String message) { this.gameSummary.add(message); }
    void clearSummary() { this.gameSummary.clear(); }
    void setIntendedAction(Action action) { this.intendedAction = action; }

    public void dealDamage(int damage) {
        this.health -= damage;
    }

    public Coord getPos() { return this.pos; }
    int getHealth() { return this.health; }
    public int getID() { return this.minionID; }
    public Player getOwner() { return this.owner; }
    public boolean isDead() { return this.health <= 0; }
    public Action getIntendedAction() { return this.intendedAction; }

    public List<String> getGameSummary() {
        return gameSummary;
    }

    public void turnReset() {
        this.intendedAction = Action.NO_ACTION;
        this.clearSummary();
    }

    public void setPathToDestination(List<Coord> pathToDest) {
        this.pathToDest = pathToDest;
    }

    public boolean isFrozen() {
        return this.timeOut > 0;
    }

    public int getTimeOut() {
        return this.timeOut;
    }

    public void decreaseTimeOut() {
        if(timeOut > 0) timeOut--;
    }

    public void addTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
