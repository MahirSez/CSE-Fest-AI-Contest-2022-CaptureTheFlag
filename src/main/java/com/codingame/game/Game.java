package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;

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
        for(Player player: gameManager.getPlayers()) {
            for(int i = 0 ; i < this.minionsPerPlayer ; i++) {
                Minion minion = new Minion(i, player);
                allMinions.add(minion);
                player.addMinion(minion);
            }
        }
    }

    ArrayList<String> getInitialInfo(Player player) {
        Player opponent = gameManager.getActivePlayers().get(player.getIndex() ^ 1);
        ArrayList<String>ret = new ArrayList<>();
        ret.add(this.maze.getRow() + " "+ this.maze.getCol());
        ret.add(player.getFlagBase().getPos().getX() + " " + player.getFlagBase().getPos().getY());
        ret.add(opponent.getFlagBase().getPos().getX() + " " + opponent.getFlagBase().getPos().getY());
        return ret;
    }
    boolean isVisible(Coord pos1, Coord pos2) {

        if(pos1.getX() == pos2.getX()) {
            int minY = Math.min(pos1.getY(), pos2.getY());
            int maxY = Math.max(pos1.getY(), pos2.getY());
            int i = pos1.getX();
            for(int j = minY ; j <= maxY ; j++) {
                if(maze.getGrid()[i][j] == 1) return false;
            }
            return true;
        }

        if(pos1.getY() == pos2.getY()) {
            int minX = Math.min(pos1.getX(), pos2.getX());
            int maxX = Math.max(pos1.getX(), pos2.getX());
            int j = pos1.getY();
            for(int i = minX ; i <= maxX ; i++) {
                if(maze.getGrid()[i][j] == 1) return false;
            }
            return true;
        }
        return false;
    }

    ArrayList<String>getGameState(Player player) {
        Player opponent = gameManager.getActivePlayers().get(player.getIndex() ^ 1);
        ArrayList<String>ret = new ArrayList<>();
        ret.add(player.getScore() + " " + opponent.getScore());
        ret.add(player.getFlag().getPos().getX() + " " + player.getFlag().getPos().getY());
        ret.add(opponent.getFlag().getPos().getX() + " " + opponent.getFlag().getPos().getY());

        ArrayList<Minion>aliveMinions = new ArrayList<>();
        for(Minion minion: player.getMinions()) {
            if(minion.health > 0) aliveMinions.add(minion);
        }
        ret.add(aliveMinions.size() + "");
        for(Minion minion: player.getMinions()) {
            ret.add(minion.getID() + " " +minion.getPos().getX() + " " + minion.getPos().getY() + " " + minion.getHealth());
        }
        ArrayList<Minion>visibleOpponents = new ArrayList<>();

        for(Minion opponentMinion: opponent.getMinions()) {
            boolean visible = false;
            for(Minion minion: player.getMinions()) {
                if(this.isVisible(opponentMinion.getPos(), minion.getPos())) {
                    visible = true;
                    break;
                }
            }
            if(visible) visibleOpponents.add(opponentMinion);
        }
        ret.add(visibleOpponents.size() + "");
        for(Minion minion: visibleOpponents) {
            ret.add(minion.getID() + " " + minion.getPos().getX() + " " + minion.getPos().getY() + " " + minion.getHealth());
        }
        return ret;
    }

    public void resetTurnData() {
        gameManager.getPlayers().forEach(Player::turnReset);
    }

    public void updateGameState() {

    }
}

/*

	my_score  opp_score

	my_flag_pos
	opp_flag_pos

	self_flag_captured
	opponent_flag_bearer

	alive minion_cnt
		id, pos, health
	seen opp-minion_cnt
		id, pos, health


 */

