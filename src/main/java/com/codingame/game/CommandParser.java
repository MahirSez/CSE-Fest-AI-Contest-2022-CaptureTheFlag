package com.codingame.game;

import com.codingame.game.action.*;
import com.codingame.game.exception.GameException;
import com.codingame.game.exception.InvalidInputException;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CommandParser {

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private Maze maze;

    static String MOVE_COMMAND = "MOVE <id> <x> <y>";
    static String WAIT_COMMAND = "WAIT <id>";
    static String FIRE_COMMAND = "FIRE <id>";
    static String FREEZE_COMMAND = "FREEZE <id>";
    static String MINE_COMMAND = "MINE <id> <x> <y>";
    String EXPECTED =
            MOVE_COMMAND +
            " or " + WAIT_COMMAND +
            " or " + FIRE_COMMAND +
            " or " + FREEZE_COMMAND +
            " or " + MINE_COMMAND;

    static final Pattern PLAYER_MOVE_PATTERN = Pattern.compile(
            "^MOVE\\s+(?<id>\\d+)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)"
                    + "(?:\\s+(?<message>.+))?"
                    + "$",
            Pattern.CASE_INSENSITIVE
    );
    static final Pattern PLAYER_WAIT_PATTERN = Pattern.compile(
            "^WAIT\\s+(?<id>\\d+)"
                    + "(?:\\s+(?<message>.+))?"
                    + "$",
            Pattern.CASE_INSENSITIVE
    );
    static final Pattern PLAYER_FIRE_PATTERN = Pattern.compile(
            "^FIRE\\s+(?<id>\\d+)"
                    + "(?:\\s+(?<message>.+))?"
                    + "$",
            Pattern.CASE_INSENSITIVE
    );
    static final Pattern PLAYER_MINE_PATTERN = Pattern.compile(
            "^MINE\\s+(?<id>\\d+)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)"
                    + "(?:\\s+(?<message>.+))?"
                    + "$",
            Pattern.CASE_INSENSITIVE
    );
    static final Pattern PLAYER_FREEZE_PATTERN = Pattern.compile(
            "^FREEZE\\s+(?<id>\\d+)"
                    + "(?:\\s+(?<message>.+))?"
                    + "$",
            Pattern.CASE_INSENSITIVE
    );
    static final Pattern PLAYER_ACTION_PATTERN = Pattern.compile(
            "^(WAIT|MOVE|FIRE|FREEZE|MINE)\\s+(?<id>\\d+).*",
            Pattern.CASE_INSENSITIVE
    );

    private void handleMineCommand(Matcher match, Minion minion) throws GameException, InvalidInputException{
        if(!match.matches()) throw new InvalidInputException(EXPECTED, "");
        int x = Integer.parseInt(match.group("x"));
        int y = Integer.parseInt(match.group("y"));
        if ( x < 0 || x >= maze.getRow() || y < 0 || y >= maze.getCol() && new Coord(x, y).manhattanTo(minion.getPos()) != 1) {
            throw new GameException(
                    String.format(
                            "Minion %d (%s) cannot place Mine at (%d, %d). Target cell has to be adjacent to minion",
                            minion.getID(),
                            minion.getOwner().getColor(),
                            x,
                            y
                    )
            );
        }
        minion.setIntendedAction(new MinePower(new Coord(x, y), minion));
    }

    private void handleMoveCommand(Matcher match, Minion minion) throws GameException, InvalidInputException {
        if(!match.matches()) throw new InvalidInputException(EXPECTED, "");
        int x = Integer.parseInt(match.group("x"));
        int y = Integer.parseInt(match.group("y"));
        if ( x < 0 || x >= maze.getRow() || y < 0 || y >= maze.getCol()) {
            throw new GameException(
                    String.format(
                            "Minion %d (%s) cannot reach its target (%d, %d) because it is out of grid!",
                            minion.getID(),
                            minion.getOwner().getColor(),
                            x,
                            y
                    )
            );
        }
        minion.setIntendedAction(new MoveAction(new Coord(x, y)));
    }

    Minion getMinionFromId(Player player, int minionID) throws GameException {
        try {
            Minion minion = player.getMinions()
                    .stream()
                    .filter( (value) -> (value.getID() == minionID))
                    .findFirst()
                    .get();
            if(minion.getIntendedAction() != Action.NO_ACTION) {
                throw new GameException(String.format("Minion %d cannot be commanded twice!", minionID));
            }
            return minion;
        } catch (NoSuchElementException e) {
            throw new GameException(String.format("Minion %d doesn't exist", minionID));
        }
    }


    private void handleWaitCommand(Minion minion) {
        minion.setIntendedAction(new WaitAction());
    }

    private void handleFireCommand(Minion minion) {
        minion.setIntendedAction(new FirePower(minion.getPos(), minion));
    }

    private void handleFreezeCommand(Minion minion) {
        minion.setIntendedAction(new FreezePower(minion.getPos(), minion));
    }

    public void parseCommands(Player player, List<String> outputs) {

        String[] commands = outputs.get(0).split("\\|");
        for(String command: commands) {
            String str = command.trim();
            if (str.isEmpty()) continue;

            try {
                Minion minion;
                Matcher match = PLAYER_ACTION_PATTERN.matcher(str);
                if (match.matches()) {
                    int minionID = Integer.parseInt(match.group("id"));
                    minion = getMinionFromId(player, minionID);
                    if(minion.isDead()) {
                        minion.addSummary(String.format("Minion %d is dead! It cannot be commanded anymore!", minionID));
                        continue;
                    }
                    else if(minion.isFrozen()) {
                        minion.addSummary(String.format("Minion %d is Frozen! It has to wait %d more moves!", minionID, minion.getTimeOut()));
                        continue;
                    }
                }
                else {
                    throw new InvalidInputException(EXPECTED, str);
                }

                if(PLAYER_MOVE_PATTERN.matcher(str).matches()) {
                    handleMoveCommand(PLAYER_MOVE_PATTERN.matcher(str), minion);
                }
                else if(PLAYER_WAIT_PATTERN.matcher(str).matches()) {
                    handleWaitCommand(minion);
                }
                else if(PLAYER_FIRE_PATTERN.matcher(str).matches()) {
                    handleFireCommand(minion);
                }
                else if(PLAYER_FREEZE_PATTERN.matcher(str).matches()) {
                    handleFreezeCommand(minion);
                }
                else if(PLAYER_MINE_PATTERN.matcher(str).matches()) {
                    handleMineCommand(PLAYER_MINE_PATTERN.matcher(str), minion);
                }
                else {
                    throw new InvalidInputException(EXPECTED, str);
                }
            } catch (InvalidInputException | GameException e) {
                deactivatePlayer(player, e.getMessage());
                gameManager.addToGameSummary("Bad command: " + e.getMessage());
                return;
            } catch (Exception e) {
                deactivatePlayer(player, new InvalidInputException(e.toString(), EXPECTED, str).getMessage());
                gameManager.addToGameSummary("Bad command: " + e.getMessage());
                return;
            }
        }

        player.getAliveMinions()
            .filter(minion -> minion.getIntendedAction() == Action.NO_ACTION && !minion.isFrozen())
            .forEach(minion ->
                minion.addSummary(String.format(
                    "Minion %d received no command.", minion.getID()
                ))
            );
    }




    private void deactivatePlayer(Player player, String message) {
        player.deactivate(escapeHTMLEntities(message));
    }

    private String escapeHTMLEntities(String message) {
        return message
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }
}
