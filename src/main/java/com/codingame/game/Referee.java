package com.codingame.game;

import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject Maze maze;
    @Inject View view;
    @Inject Game game;
    @Inject RandomUtil randomUtil;

    @Override
    public void init() {

        randomUtil.init(gameManager.getSeed());
        maze.init();
        game.init();
        view.init();
        this.sendInitialInfo();
    }


    void sendInitialInfo() {
        for(Player player: gameManager.getPlayers()) {
            for(String line: game.getInitialInfo(player)) {
                player.sendInputLine(line);
            }
        }
    }
    void sendGameState() {
        for(Player player: gameManager.getPlayers()) {
            for(String line: game.getGameState(player)) {
                player.sendInputLine(line);
            }
            player.execute();
        }
    }

    @Override
    public void gameTurn(int turn) {
        this.sendGameState();

        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
                // Check validity of the player output and compute the new game state
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
            }
        }
    }
}
