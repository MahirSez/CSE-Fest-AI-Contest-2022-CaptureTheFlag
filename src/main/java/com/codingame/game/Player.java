package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.ArrayList;
import java.util.stream.Stream;


public class Player extends AbstractMultiplayerPlayer {
    private int currentCredit;
    private final ArrayList<Minion> minions;
    private final Flag flag;
    private final FlagBase flagBase;
    private boolean isWinner;

    private boolean leftPlayer;
    private boolean timeout;

    Player() {
        this.minions = new ArrayList<>();
        this.flag = new Flag();
        this.flagBase = new FlagBase();
        this.currentCredit = Config.INITIAL_CREDIT;
    }

    void addCredit(int add) {
        currentCredit += add;
    }
    public int getCurrentCredit() {
        return currentCredit;
    }

    void setLeftPlayer(boolean isLeftPlayer) { this.leftPlayer = isLeftPlayer; }

    void addMinion(Minion minion) { minions.add(minion); }
    
    public ArrayList<Minion>getMinions() { return this.minions; }

    public boolean isLeftPlayer() { return this.leftPlayer; }

    Minion getMinion(int id) { return this.minions.get(id); }

    public Flag getFlag() { return this.flag; }

    FlagBase getFlagBase() { return this.flagBase; }

    public String getColor() { return this.leftPlayer ? Config.LEFT_PLAYER_COLOR_NAME : Config.RIGHT_PLAYER_COLOR_NAME ; }

    public Stream<Minion> getAliveMinions() {
        return this.minions.stream().filter(minion -> !minion.isDead());
    }

    public void turnReset() {
        minions.forEach(Minion::turnReset);
    }

    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player

        // TODO: Replace the returned value with a valid number. Most of the time the value is 1. 
        return 1;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }
    public boolean isWinner() {
        return this.isWinner;
    }

    public boolean isTimedOut() {
        return this.timeout;
    }
    public void setTimedOut(boolean timeout) {
        this.timeout = timeout;
    }

    public void decreaseCredit(int price) {
        this.currentCredit -= price;
    }
}
