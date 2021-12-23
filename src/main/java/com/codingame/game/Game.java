package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Random;

@Singleton
public class Game {

    int totalMinions, minionsPerPlayer;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private Maze maze;
    @Inject private RandomUtil randomUtil;
    ArrayList<Minion>allMinions;




    void init() {
        this.randomUtil.init(this.gameManager.getSeed());

        this.minionsPerPlayer = RandomUtil.randomInt(Config.MIN_MINIONS, Config.MAX_MINIONS);
        this.totalMinions = minionsPerPlayer * gameManager.getPlayerCount();

        generateMinions();
        setMinionsPositions();
    }

    void setMinionsPositions() {
        int leftPlayer = RandomUtil.randomInt(0, 1);
        int rightPlayer = (leftPlayer ^ 1);
        gameManager.getPlayer(leftPlayer).setLeftPlayer(true);
        gameManager.getPlayer(rightPlayer).setLeftPlayer(false);

        int leftColumn = 0, rightColumn = maze.getCol() - 1;
        System.out.println(" ?? " + leftColumn + " " + rightColumn);

        for(int i = 0 ; i < this.minionsPerPlayer ; i++) {
            gameManager.getPlayer(0).getMinion(i).setPosition(new Coord(i, leftColumn));
            gameManager.getPlayer(1).getMinion(i).setPosition(new Coord(i, rightColumn));
        }
    }

    void generateMinions() {
        allMinions = new ArrayList<>();
        int minionId = 0;
        for(Player player: gameManager.getPlayers()) {
            for(int i = 0 ; i < this.minionsPerPlayer ; i++) {
                Minion minion = new Minion(minionId++, player);
                allMinions.add(minion);
                player.addMinion(minion);
            }
        }
    }

    ArrayList<String> getGlobalInfo() {
        ArrayList<String>ret = new ArrayList<>();
        return ret;
    }
}
