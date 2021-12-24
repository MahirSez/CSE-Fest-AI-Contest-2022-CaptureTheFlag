package com.codingame.game;

import com.codingame.game.action.Action;
import com.codingame.game.action.MoveAction;
import com.codingame.game.action.WaitAction;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

import javax.inject.Singleton;
import java.util.Arrays;
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
    String EXPECTED = MOVE_COMMAND + " or " + WAIT_COMMAND;

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

    static final Pattern PLAYER_ACTION_PATTERN = Pattern.compile(
            "^(WAIT|MOVE)\\s+(?<id>\\d+).*",
            Pattern.CASE_INSENSITIVE
    );


    private void handleMoveCommand(Matcher match, Minion minion) throws GameException, InvalidInputException {
        if(!match.matches()) throw new InvalidInputException(EXPECTED, "");
        int x = Integer.parseInt(match.group("x"));
        int y = Integer.parseInt(match.group("y"));
        if ( x < 0 || x >= maze.getRow() || y < 0 || y >= maze.getCol()) {
            throw new GameException(
                    String.format(
                            "Pac %d (%s) cannot reach its target (%d, %d) because it is out of grid!",
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
            if(minion.isDead()) {
                minion.addSummary(String.format("Minion %d is dead! It cannot be commanded anymore!", minionID));
            }
            else if(minion.getIntendedAction() != Action.NO_ACTION) {
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
            .filter(minion -> minion.getIntendedAction() == Action.NO_ACTION)
            .forEach(minion ->
                minion.addSummary(String.format(
                    "Pac %d received no command.", minion.getID()
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
