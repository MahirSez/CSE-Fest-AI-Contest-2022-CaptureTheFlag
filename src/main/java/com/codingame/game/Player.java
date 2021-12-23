package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.ArrayList;


public class Player extends AbstractMultiplayerPlayer {


    private final ArrayList<Minion> minions;
    private final Flag flag;
    private final FlagBase flagBase;

    private boolean leftPlayer;
    Player() {
        this.minions = new ArrayList<>();
        this.flag = new Flag();
        this.flagBase = new FlagBase();
    }

    void setLeftPlayer(boolean isLeftPlayer) { this.leftPlayer = isLeftPlayer; }
    void addMinion(Minion minion) { minions.add(minion); }
    
    ArrayList<Minion>getMinions() { return this.minions; }
    boolean isLeftPlayer() { return this.leftPlayer; }
    Minion getMinion(int id) { return this.minions.get(id); }
    Flag getFlag() { return this.flag; }
    FlagBase getFlagBase() { return this.flagBase; }

    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player

        // TODO: Replace the returned value with a valid number. Most of the time the value is 1. 
        return 1;
    }
}
