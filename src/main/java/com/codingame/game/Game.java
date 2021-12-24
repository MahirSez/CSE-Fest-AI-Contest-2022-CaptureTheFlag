package com.codingame.game;

import com.codingame.game.action.ActionType;
import com.codingame.game.action.MoveAction;
import com.codingame.game.view.View;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class Game {

    int totalMinions, minionsPerPlayer;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private Maze maze;
    @Inject private View view;
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
            gameManager.getPlayer(leftPlayer).getMinion(i).setPos(new Coord(offset + i, leftColumn));
            gameManager.getPlayer(rightPlayer).getMinion(i).setPos(new Coord(offset + i, rightColumn));
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

    private void updateMovement() {
        for(Minion minion: allMinions) {
            if(minion.getIntendedAction().getActionType() == ActionType.MOVE) {
                Coord from = minion.getPos();
                Coord to = ((MoveAction)minion.getIntendedAction()).getDestination();
                List<Coord>path = this.computeShortestPath(from, to);
                minion.setPathToDestination(path);

                if(path.size() == 0) {
                    minion.addSummary(String.format(
                        "(%d, %d) is unreachable for Minion %d",
                        to.getX(),
                        to.getY(),
                        minion.getID()
                    ));
                }
                else if(path.size() == 1) {
                    minion.addSummary(String.format(
                        "Minion %d is already at (%d, %d)",
                        minion.getID(),
                        to.getX(),
                        to.getY()
                    ));
                }
                else  {
                    view.moveMinion(minion, minion.getPos(), path.get(1));
                    minion.setPos(path.get(1));
                    minion.addSummary(String.format(
                        "Minion %d moved to (%d, %d).",
                        minion.getID(),
                        minion.getPos().x,
                        minion.getPos().y
                        )
                    );

                }
            }
        }
    }

    private List<Coord> computeShortestPath(Coord from, Coord to) {
        int n = maze.getRow();
        int m = maze.getCol();
        Coord[][] parent = new Coord[n][m];
        int[][] distance = new int[n][m];

        for(int[] row: distance) {
            Arrays.fill(row, Config.INF);
        }
        Queue<Coord> queue = new LinkedList<>();
        queue.add(from);
        distance[from.getX()][from.getY()] = 0;

        while(!queue.isEmpty()) {
            Coord top = queue.remove();
            int topDistance = distance[top.getX()][top.getY()];
            for(Coord adj: Config.ADJACENCY) {
                int x = top.getX() + adj.getX();
                int y = top.getY() + adj.getY();
                if(x >=0 && x < n && y >=0 && y < m && maze.getGrid()[x][y] == 0 && distance[x][y] == Config.INF) {
                    distance[x][y] = topDistance + 1;
                    parent[x][y] = top;
                    Coord newCoord = new Coord(x, y);
                    if(newCoord.equals(to)) break;
                    queue.add(newCoord);
                }
            }
        }
        List<Coord>path = new ArrayList<>();
        if(distance[to.getX()][to.getY()] == Config.INF) return path;

        path.add(to);
        System.out.println("From " + from + " To " + to);
        while(!to.equals(from)) {
            to = parent[to.getX()][to.getY()];
            path.add(to);
        }
        Collections.reverse(path);
//        System.out.println(path);
        return path;
    }


    public void updateGameState() {
        this.updateMovement();
        this.printGameSummary();
    }

    private void printGameSummary() {
        for (Player player : gameManager.getPlayers()) {
            if (player.getMinions().stream().anyMatch(minion -> !minion.getGameSummary().isEmpty())) {
                gameManager.addToGameSummary(String.format("%s:", player.getNicknameToken()));
                player.getMinions().stream()
                    .sorted(Comparator.comparing(Minion::getID))
                    .forEach(minion -> {
                        minion.getGameSummary().forEach(line -> {
                            gameManager.addToGameSummary("- " + line);
                        });
                        minion.clearSummary();
                    });
            }
        }
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

