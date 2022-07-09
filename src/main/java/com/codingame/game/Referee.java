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
        Player player0 = gameManager.getPlayer(0);
        Player player1 = gameManager.getPlayer(1);
        boolean player0_CapturedFlag = player1.getFlag().getPos().equals(player0.getFlagBase().getPos());
        boolean player1_CapturedFlag = player0.getFlag().getPos().equals(player1.getFlagBase().getPos());
        int player0_Credit = player0.getCurrentCredit();
        int player1_Credit = player1.getCurrentCredit();
        int player0_AliveCount = (int) player0.getMinions().stream().filter(minion -> !minion.isDead()).count();
        int player1_AliveCount = (int) player1.getMinions().stream().filter(minion -> !minion.isDead()).count();

        // 200 turns have been completed without anyone being winner
        if(!player0.isWinner() && !player1.isWinner()) {
            if(player0_AliveCount > player1_AliveCount) {
                player0.setScore(1);
                player1.setScore(0);
                player0.setWinner(true);
            }
            else if(player1_AliveCount > player0_AliveCount) {
                player1.setScore(1);
                player0.setScore(0);
                player1.setWinner(true);
            }
            else if(player0_Credit > player1_Credit) {
                player0.setScore(1);
                player1.setScore(0);
                player0.setWinner(true);
            }
            else if(player1_Credit > player0_Credit) {
                player1.setScore(1);
                player0.setScore(0);
                player1.setWinner(true);
            }
            // no one is winner
            else {
                player1.setScore(0);
                player0.setScore(0);
            }
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
                            Player opponent = game.getOpponentOf(player);
                            boolean iCapturedFlag = opponent.getFlag().getPos().equals(player.getFlagBase().getPos());
                            boolean oppCapturedFlag = player.getFlag().getPos().equals(opponent.getFlagBase().getPos());
                            int myScore = player.getCurrentCredit();
                            int oppScore = opponent.getCurrentCredit();
                            int myAliveCount = (int) player.getMinions().stream().filter(minion -> !minion.isDead()).count();
                            int oppAliveCount = (int) opponent.getMinions().stream().filter(minion -> !minion.isDead()).count();

                            if(player.isWinner()) {
                                if(iCapturedFlag && oppCapturedFlag) {
                                    if(myAliveCount != oppAliveCount) return "Minions Alive: " + myAliveCount;
                                    if(myScore != oppScore) return "Score: " + myScore;
                                    return "-";
                                }
                                if(iCapturedFlag)
                                    return "Captured Flag";

                                if(oppAliveCount == 0)
                                    return "All Opponent Dead";

                                return "-";
                            }
                            else if(opponent.isWinner()) {
                                if(iCapturedFlag && oppCapturedFlag) {
                                    if(myAliveCount != oppAliveCount) return "Minions Alive: " + myAliveCount;
                                    if(myScore != oppScore) return "Score: " + myScore;
                                    return "-";
                                }
                                return  "-";
                            }

                            if(player.isTimedOut())
                                return "Timed Out!";

                            if(!player.isWinner() && !opponent.isWinner())
                                return "Score: " + myScore;

                            return "-";
                        })
                        .collect(Collectors.toList())
                        .toArray(new String[2])
        );
    }
}
