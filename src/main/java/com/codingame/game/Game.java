package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class Game {

    int totalMinions, minionsPerPlayer;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private Maze maze;
    ArrayList<Minion>allMinions;




    void init() {

        this.minionsPerPlayer = RandomUtil.randomInt(Config.MIN_MINIONS, Config.MAX_MINIONS);
        this.totalMinions = minionsPerPlayer * gameManager.getPlayerCount();

        generateMinions();
        setMinionsPositions();
        setFlagBasePosition();
        setFlagPosition();
    }
    void setFlagBasePosition() {
        for(Player player: gameManager.getPlayers()) {
            if(player.isLeftPlayer()) {
                player.getFlagBase().setPos(new Coord(maze.getRow() / 2, 0));
            }
            else {
                player.getFlagBase().setPos(new Coord(maze.getRow()/2, maze.getCol() - 1));
            }
        }
    }

    void setFlagPosition() {
        for(Player player: gameManager.getPlayers()) {
            player.getFlag().setPos(player.getFlagBase().getPos());
        }
    }


    void setMinionsPositions() {
        int leftPlayer = RandomUtil.randomInt(0, 1);
        int rightPlayer = (leftPlayer ^ 1);
        gameManager.getPlayer(leftPlayer).setLeftPlayer(true);
        gameManager.getPlayer(rightPlayer).setLeftPlayer(false);

        int leftColumn = 1, rightColumn = maze.getCol() - 2;
        int offset = maze.getRow()/2 - this.minionsPerPlayer/2;

        for(int i = 0 ; i < this.minionsPerPlayer ; i++) {
            gameManager.getPlayer(leftPlayer).getMinion(i).setPosition(new Coord(offset + i, leftColumn));
            gameManager.getPlayer(rightPlayer).getMinion(i).setPosition(new Coord(offset + i, rightColumn));
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

    ArrayList<String> getInitialInfo(Player self, Player opponent) {
        ArrayList<String>ret = new ArrayList<>();
        ret.add(this.maze.getRow() + " "+ this.maze.getCol());
        ret.add(self.getFlagBase().getPos().getX() + " " + self.getFlagBase().getPos().getY());
        ret.add(opponent.getFlagBase().getPos().getX() + " " + opponent.getFlagBase().getPos().getY());
        return ret;
    }
}

