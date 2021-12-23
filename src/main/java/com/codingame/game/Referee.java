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
        Player player0 = gameManager.getPlayer(0);
        Player player1 = gameManager.getPlayer(1);
        for(String line: game.getInitialInfo(player0, player1)) player0.sendInputLine(line);
        for(String line: game.getInitialInfo(player1, player0)) player1.sendInputLine(line);
    }

    @Override
    public void gameTurn(int turn) {
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine("input");
            player.execute();
        }

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
