package com.codingame.game;

import java.util.List;
import java.util.stream.Collectors;

import com.codingame.game.view.View;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject Maze maze;
    @Inject View view;
    @Inject Game game;
    @Inject RandomUtil randomUtil;
    @Inject CommandParser commandParser;
    @Inject private EndScreenModule endScreenModule;

    boolean gameOverFrame;

    @Override
    public void init() {
        gameManager.setFrameDuration(Config.FRAME_DURATION);

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
    void sendGameStateToPlayers() {

        for(Player player: gameManager.getPlayers()) {
            for(String line: game.getGameState(player)) {
                player.sendInputLine(line);
            }
        }
        for(Player player: gameManager.getPlayers()) {
            player.execute();
        }
    }


    private void handlePlayerCommands() {
        for (Player player : gameManager.getPlayers()) {
            try {
                List<String> outputs = player.getOutputs();
                commandParser.parseCommands(player, outputs);
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
                player.setTimedOut(true);
                gameManager.addToGameSummary(player.getNicknameToken() + " has not provided " + player.getExpectedOutputLines() + " lines in time");
            }
        }
    }

    @Override
    public void gameTurn(int turn) {
        game.resetTurnData();

        if(!gameOverFrame) {
            sendGameStateToPlayers();
            handlePlayerCommands();
        }

        view.resetData();
        game.updateGameState();
        view.updateFrame();

        if(this.gameOverFrame) {
            game.endGame();
            gameManager.endGame();
        }
        else if(game.isGameOver()) {
            this.gameOverFrame = true;
        }

    }

    @Override
    public void onEnd() {
        if(!gameManager.getPlayer(0).isWinner() && !gameManager.getPlayer(1).isWinner()) {
            gameManager.getPlayers().forEach(player -> player.setScore(player.getCurrentCredit()));
        }
        else {
            gameManager.getPlayers().forEach(player -> player.setScore(player.isWinner()? 1: 0));
        }
//        endScreenModule.setTitleRankingsSprite("tank1/player1.png");

        endScreenModule.setScores(
                gameManager.getPlayers()
                        .stream()
                        .mapToInt(AbstractMultiplayerPlayer::getScore)
                        .toArray(),

                gameManager.getPlayers()
                        .stream()
                        .map(player -> {
                            if( game.getOpponentOf(player).getFlag().getPos().equals(player.getFlagBase().getPos()) ) {
                                return "Captured Flag!";
                            }
                            if( (int) player.getMinions().stream().filter(minion -> !minion.isDead()).count() == 0) {
                                return "All Minions Dead!";
                            }
                            if(player.isTimedOut()) {
                                return "Timed Out!";
                            }
                            if(!player.isWinner() && !game.getOpponentOf(player).isWinner()) {
                                return player.getCurrentCredit() + " credit";
                            }
                            return "-";
                        })
                        .collect(Collectors.toList())
                        .toArray(new String[2])
        );
    }
}
