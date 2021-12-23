package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.ArrayList;


public class Player extends AbstractMultiplayerPlayer {

    private boolean isLeftPlayer;
    private ArrayList<Minion> minions;
    Player() {
        this.minions = new ArrayList<Minion>();
    }

    void setLeftPlayer(boolean isLeftPlayer) { this.isLeftPlayer = isLeftPlayer; }
    boolean getIsLeftPlayer() { return this.isLeftPlayer; }

    void addMinion(Minion minion) {
        minions.add(minion);
    }
    
    ArrayList<Minion>getMinions() { return this.minions; }

    Minion getMinion(int id) { return this.minions.get(id); }

    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player

        // TODO: Replace the returned value with a valid number. Most of the time the value is 1. 
        return 1;
    }
}
