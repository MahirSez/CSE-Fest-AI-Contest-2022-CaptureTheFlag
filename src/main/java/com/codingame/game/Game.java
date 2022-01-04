package com.codingame.game;

import com.codingame.game.action.ActionType;
import com.codingame.game.action.MoveAction;
import com.codingame.game.action.PowerUp;
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
    ArrayList<Minion> aliveMinions;

    void init() {

        this.minionsPerPlayer = RandomUtil.randomOddInt(Config.MIN_MINIONS, Config.MAX_MINIONS);
        this.totalMinions = minionsPerPlayer * gameManager.getPlayerCount();

        setPlayerSide();
        generateMinions();
//        setMinionsPositions();
        setFlagBasePosition();
        setFlagPosition();
        setMinionsPositionsRandom();
        generateCoins();
    }

    private void generateCoins() {
        int row = maze.getRow();
        int col = maze.getCol();
        ArrayList<Coin> availableCoins = maze.getAvailableCoins();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j <= col/2; j++) {
                if (maze.getGrid()[i][j] == 1) continue;
                boolean occupied = false;
                for (Player player : gameManager.getPlayers()) {
                    for (Minion minion : player.getMinions()) {
                        if (minion.getPos().manhattanTo(i, j) == 0) {
                            occupied = true;
                        }
                    }
                    if (player.getFlagBase().getPos().manhattanTo(i, j) == 0) {
                        occupied = true;
                    }
                }
                if (!occupied) {
//                    int coinValue = Config.COIN_VALUES[RandomUtil.randomWeightedIndex(Config.COIN_WEIGHTS)];
                    int coinValue = Config.COIN_VALUES[0];
                    availableCoins.add(new Coin(new Coord(i, j), coinValue));
                    if (j < col-1-j) {
                        availableCoins.add(new Coin(new Coord(i, col-1-j), coinValue));
                    }
                }
            }
        }
    }

    public ArrayList<Minion> getAliveMinions() {
        return this.aliveMinions;
    }

    private void setPlayerSide() {
        gameManager.getPlayer(0).setLeftPlayer(true);
        gameManager.getPlayer(1).setLeftPlayer(false);
    }

    private void setMinionsPositionsRandom() {

        int midCol = maze.getCol()/2;
        List<Coord>freeSpaces = new ArrayList<>();
        for(int i = 0 ; i < maze.getRow() ; i++) {
            for(int j = 0 ; j < midCol ; j++) {
                if(maze.getGrid()[i][j] == 0) freeSpaces.add(new Coord(i,j));
            }
        }

        freeSpaces.remove(gameManager.getPlayer(0).getFlagBase().getPos());

        for(int i = 0 ; i < minionsPerPlayer ; i++) {
            int id = RandomUtil.randomInt(0, freeSpaces.size());
            Coord leftCoord = freeSpaces.get(id);
            Coord rightCoord = new Coord(leftCoord.getX(), maze.getCol() - leftCoord.getY() - 1);
            gameManager.getPlayer(0).getMinion(i).setPos(leftCoord);
            gameManager.getPlayer(1).getMinion(i).setPos(rightCoord);
            freeSpaces.remove(leftCoord);
        }

    }

    void setFlagBasePosition() {

        for(int row = maze.getRow()/ 2 ; row < maze.getRow() ; row++) {
            if(maze.getGrid()[row][1] == 0) {
                for (Player player : gameManager.getPlayers()) {
                    if (player.isLeftPlayer()) {
                        player.getFlagBase().setPos(new Coord(row, 1));
                    } else {
                        player.getFlagBase().setPos(new Coord(row, maze.getCol() - 2));
                    }
                }
                break;
            }
        }
    }

    void setFlagPosition() {
        for(Player player: gameManager.getPlayers()) {
            player.getFlag().setPos(player.getFlagBase().getPos());
        }
    }


    void setMinionsPositions() {
        int leftPlayer = 0;
        int rightPlayer = (leftPlayer ^ 1);
        gameManager.getPlayer(leftPlayer).setLeftPlayer(true);
        gameManager.getPlayer(rightPlayer).setLeftPlayer(false);

        int leftColumn = 1, rightColumn = maze.getCol() - 2;
        int midPos = maze.getRow()/2;

        List<Integer>freeRows = new ArrayList<>();

        for(int row = midPos, minionLeft = minionsPerPlayer/2 + 1; minionLeft > 0 ; row++) {
            if(maze.getGrid()[row][leftColumn] == 0) {
                freeRows.add(row);
                minionLeft--;
            }
        }
        for(int row = midPos - 1 , minionLeft = minionsPerPlayer/2 ; minionLeft > 0 ; row--) {
            if(maze.getGrid()[row][leftColumn] == 0) {
                freeRows.add(row);
                minionLeft--;
            }
        }
        Collections.sort(freeRows);
        for(int i = 0 ; i < minionsPerPlayer ; i++) {
            gameManager.getPlayer(leftPlayer).getMinion(i).setPos(new Coord(freeRows.get(i), leftColumn));
            gameManager.getPlayer(rightPlayer).getMinion(i).setPos(new Coord(freeRows.get(i), rightColumn));
        }
    }

    void generateMinions() {
        aliveMinions = new ArrayList<>();
        for(Player player: gameManager.getPlayers()) {
            for(int i = 0 ; i < this.minionsPerPlayer ; i++) {
                Minion minion = new Minion(i, player);
                aliveMinions.add(minion);
                player.addMinion(minion);
            }
        }
    }

    ArrayList<String> getInitialInfo(Player player) {
        Player opponent = getOpponentOf(player);
        ArrayList<String>ret = new ArrayList<>();
        ret.add(this.maze.getRow() + " "+ this.maze.getCol());
        for(int[] row: this.maze.getGrid()) {
            StringBuilder str = new StringBuilder();
            for(int cell: row) str.append(cell == 1 ? "#": ".");
            ret.add(str.toString());
        }
        ret.add(player.getFlagBase().getPos().getX() + " " + player.getFlagBase().getPos().getY());
        ret.add(opponent.getFlagBase().getPos().getX() + " " + opponent.getFlagBase().getPos().getY());
        return ret;
    }


    ArrayList<String>getGameState(Player player) {
        Player opponent = gameManager.getPlayers().get(player.getIndex() ^ 1);
        ArrayList<String>ret = new ArrayList<>();
        ret.add(player.getScore() + " " + opponent.getScore());
        ret.add(player.getFlag().getPos().getX() + " " + player.getFlag().getPos().getY() + " " + (player.getFlag().isCaptured() ? 1 : 0) );
        ret.add(opponent.getFlag().getPos().getX() + " " + opponent.getFlag().getPos().getY() + " " + (opponent.getFlag().isCaptured() ? 1 : 0));

        ArrayList<Minion>aliveMinions = new ArrayList<>();
        for(Minion minion: player.getMinions()) {
            if( !minion.isDead()) aliveMinions.add(minion);
        }
        ret.add(aliveMinions.size() + "");
        for(Minion minion: aliveMinions) {
            ret.add(minion.getID() + " " +minion.getPos().getX() + " " + minion.getPos().getY() + " " + minion.getHealth());
        }
        ArrayList<Minion>visibleOpponents = new ArrayList<>();

        for(Minion opponentMinion: opponent.getMinions()) {
            boolean visible = false;
            for(Minion minion: player.getMinions()) {
                if(!minion.isDead() && !opponentMinion.isDead() && maze.isVisible(opponentMinion.getPos(), minion.getPos())) {
                    visible = true;
                    break;
                }
            }
            if(visible) {
                visibleOpponents.add(opponentMinion);
                System.out.println(player.getColor() + " can see minion at " + opponentMinion.getPos());
            }
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

    private void updateMinionMovement() {
        for(Minion minion: aliveMinions) {
            if(minion.getIntendedAction().getActionType() == ActionType.MOVE) {
                Coord from = minion.getPos();
                Coord to = ((MoveAction)minion.getIntendedAction()).getDestination();
                List<Coord>path = this.computeShortestPath(from, to);
                minion.setPathToDestination(path);

                if(path.size() == 0) {
                    minion.addSummary(String.format("(%d, %d) is unreachable for Minion %d", to.getX(), to.getY(), minion.getID()));
                }
                else if(path.size() == 1) {
                    minion.addSummary(String.format("Minion %d is already at (%d, %d)", minion.getID(), to.getX(), to.getY()));
                }
                else  {
                    view.moveMinion(minion);
                    minion.setPos(path.get(1));
                    minion.addSummary(String.format("Minion %d moved to (%d, %d)", minion.getID(), minion.getPos().x, minion.getPos().y));
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
        while(!to.equals(from)) {
            to = parent[to.getX()][to.getY()];
            path.add(to);
        }
        Collections.reverse(path);
        return path;
    }

    private void updateCoins() {
        ArrayList<Coin> acquiredCoins = new ArrayList<>();
        for (Coin coin : maze.getAvailableCoins()) {
            boolean acquired = false;
            for (Player player : gameManager.getPlayers()) {
                for (Minion minion : player.getMinions()) {
                    if (minion.isDead()) continue;
                    if (minion.getPos().manhattanTo(coin.getPosition()) == 0) {
                        player.addCredit(coin.getValue());
                        acquired = true;
                        break;
                    }
                }
            }
            if (acquired) {
                acquiredCoins.add(coin);
            }
        }
        view.removeCoins(acquiredCoins);
        maze.getAvailableCoins().removeAll(acquiredCoins);
    }

    public void updateGameState() {

        updateMinionTimeOut();
        updateMinionMovement();
        updateCoins();
        updateDamage();
        removeDeadMinions();
        updateFlagPosition();
        printGameSummary();

        for(Player player: gameManager.getPlayers()) {
            System.out.println(player.getColor());
            for(Minion minion: player.getMinions()) {
                System.out.println(minion.getID() + " -> "  + minion.getPos() +  " = " + minion.getHealth());
            }
        }
    }

    private void updateMinionTimeOut() {
        for(Minion minion: aliveMinions) {
            minion.decreaseTimeOut();
        }
    }

    private void removeDeadMinions() {
        List<Minion>deadMinions = new ArrayList<>();
        for(Minion minion: aliveMinions) {
            if(minion.isDead()) {
                deadMinions.add(minion);
                view.addDeadMinion(minion);
            }
        }
        aliveMinions.removeAll(deadMinions);
    }

    private void updateDamage() {

        for(Minion minion: aliveMinions) {
            if(minion.getIntendedAction().getActionType() == ActionType.POWER_UP) {
                PowerUp power = (PowerUp) minion.getIntendedAction();
                if(!power.canBuy(minion.getOwner())) {
                    minion.addSummary(String.format("Cannot buy power %s, not enough credit available", power.getPowerType()));
                }
                else {
                    minion.addSummary(String.format("Minion %d is using power: %s", minion.getID(), power.getPowerType()));
                    minion.getOwner().decreaseCredit(power.getPrice());
                    List<Minion>affectedMinions = power.damageMinions(this, maze);
                    view.addAffectedMinions(affectedMinions, power.getPowerType());
                    view.addPowerUpUser(minion, power.getPowerType());
                }
            }
        }
    }

    Player getOpponentOf(Player player) {
        return gameManager.getPlayers().get(player.getIndex() ^ 1);
    }

    private void updateFlagPosition() {
        for(Player player: gameManager.getPlayers()) {
            Flag flag = player.getFlag();
            if(flag.isCaptured() && !flag.getCarrier().isDead()) {
                flag.setPos(flag.getCarrier().getPos());
            }
            else {
                Player opponent = getOpponentOf(player);
                for(Minion minion: opponent.getMinions()) {
                    if(!minion.isDead() && minion.getPos().equals(flag.getPos())) {
                        flag.setCarrier(minion);
                        flag.setPos(flag.getCarrier().getPos());
                        break;
                    }
                }
            }

        }
    }

    private void printGameSummary() {
        for (Player player : gameManager.getPlayers()) {
            gameManager.addToGameSummary("Player " + player.getNicknameToken() + " credits: " + player.getCurrentCredit());
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

    public boolean isGameOver() {
        boolean gameOver = false;
        for(Player player: gameManager.getPlayers()) {
            if(player.getFlag().getPos().equals(getOpponentOf(player).getFlagBase().getPos())) {
                getOpponentOf(player).setWinner(true);
                gameOver = true;
            }
        }
        for(Player player: gameManager.getPlayers()) {
            if( (int) player.getMinions().stream().filter(minion -> !minion.isDead()).count() == 0) {
                getOpponentOf(player).setWinner(true);
                gameOver = true;
            }
        }
        return gameOver;
    }

    public void endGame() {
        int flagReachedCnt = 0;
        Player winner = null;
        for(Player player: gameManager.getPlayers()) {
            if(player.getFlag().getPos().equals(getOpponentOf(player).getFlagBase().getPos())) {
                flagReachedCnt++;
                gameManager.addToGameSummary(String.format("%s has captured the flag!", getOpponentOf(player).getNicknameToken()));
                winner = getOpponentOf(player);
            }
        }
        if(flagReachedCnt == 2) {
            gameManager.addToGameSummary("Both players have captured opponent's flag at the same time\nMatch tied!");
        }
        else if(flagReachedCnt == 1) {
            gameManager.addToGameSummary(String.format("%s is the winner", winner.getNicknameToken()));
        }
        else {
            for (Player player : gameManager.getPlayers()) {
                if ((int) player.getMinions().stream().filter(minion -> !minion.isDead()).count() == 0) {
                    gameManager.addToGameSummary(String.format("%s has no minions left! %s is the winner", player.getNicknameToken(), getOpponentOf(player).getNicknameToken()));
                }
            }
        }
    }
}
