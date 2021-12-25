package com.codingame.game.view;

import com.codingame.game.*;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class View {

    @Inject Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;
    HashMap<Minion, Circle>minionToCircle;
    HashMap<Flag, Sprite>flagToSprite;

    List<Minion> movers;

    World world;

    int wallWidth, wallHeight;

    public void drawOuterRectangle() {
        graphicEntityModule.createRectangle()
                .setHeight(world.getHeight() - Config.MAZE_UPPER_OFFSET)
                .setWidth(world.getWidth())
                .setX(0)
                .setY(Config.MAZE_UPPER_OFFSET);
    }
    int toPixelCenterX(int x) {
        return this.toPixelCornerX(x) + this.wallWidth / 2;
    }
    int toPixelCenterY(int y) {
        return this.toPixelCornerY(y) + this.wallHeight / 2;
    }
    int toPixelCornerX(int x) {
        return x * wallWidth;
    }
    int toPixelCornerY(int y) {
        return Config.MAZE_UPPER_OFFSET + wallHeight * y;
    }

    public void drawMinions() {
         for(Player player: gameManager.getPlayers()) {
             for(Minion minion: player.getMinions()) {
                Coord coord = minion.getPos();
                Circle circle = graphicEntityModule.createCircle()
                        .setRadius( (int) (this.wallHeight*0.8 /2))
                        .setLineWidth(0)
                        .setX(this.toPixelCenterX(coord.getY()))
                        .setY(this.toPixelCenterY(coord.getX()))
                        .setLineColor(0)
                        .setLineWidth(2);

                if(player.isLeftPlayer()) circle.setFillColor(Config.LEFT_PLAYER_COLOR);
                else circle.setFillColor(Config.RIGHT_PLAYER_COLOR);

                this.minionToCircle.put(minion, circle);
             }
         }
    }

    public void drawBackground() {
        // add background image / texture
    }

    public void drawMaze(int row, int col, int[][] grid) {

        for(int i = 0 ; i < row ; i++) {
            for(int j = 0 ; j < col ; j++) {
                int x = this.toPixelCornerX(j);
                int y = this.toPixelCornerY(i);
                Rectangle cellBlock = graphicEntityModule.createRectangle()
                        .setHeight(wallHeight)
                        .setWidth(wallWidth)
                        .setX(x)
                        .setY(y)
                        .setLineColor(0)
                        .setLineWidth(2);
                if(grid[i][j] == 1) {
                    cellBlock.setFillColor(Config.WALL_COLOR);
                }
                tooltips.setTooltipText(cellBlock, i + " , " + j);
            }
        }
    }

    private void drawFlags() {
        for(Player player: gameManager.getPlayers()) {

            int x = this.toPixelCornerX(player.getFlag().getPos().getY());
            int y = this.toPixelCornerY(player.getFlag().getPos().getX());
            Rectangle cellBlock = graphicEntityModule.createRectangle()
                    .setHeight(wallHeight)
                    .setWidth(wallWidth)
                    .setX(x)
                    .setY(y)
                    .setZIndex(1);

            Sprite flag = graphicEntityModule.createSprite()
                    .setImage("flag.png")
                    .setBaseWidth(this.wallWidth)
                    .setBaseHeight(this.wallHeight)
                    .setX(x)
                    .setY(y)
                    .setZIndex(2);


            if(player.isLeftPlayer()) {
                flag.setTint(Config.LEFT_PLAYER_COLOR);
                cellBlock.setLineColor(Config.LEFT_PLAYER_COLOR);
            }
            else {
                flag.setTint(Config.RIGHT_PLAYER_COLOR);
                cellBlock.setLineColor(Config.RIGHT_PLAYER_COLOR);
            }
            cellBlock.setLineWidth(7);

            flagToSprite.put(player.getFlag(), flag);
        }
    }

    public void init() {
        this.world = graphicEntityModule.getWorld();


        int[][] grid = maze.getGrid();
        int row = grid.length;
        int col = grid[0].length;

        this.wallWidth = world.getWidth() / col;
        this.wallHeight = (world.getHeight() - Config.MAZE_UPPER_OFFSET) / row;

        this.movers = new ArrayList<>();
        this.minionToCircle = new HashMap<>();
        this.flagToSprite = new HashMap<>();

        drawBackground();
        drawOuterRectangle();
        drawMaze(row, col, grid);
        drawMinions();
        drawFlags();
    }
    public void resetData() {
        movers.clear();
    }

    public void updateFrame() {
        performMoves();
        updateFlag();
    }

    private void updateFlag() {
        for(Player player: gameManager.getPlayers()) {
            Sprite flag = this.flagToSprite.get(player.getFlag());
            flag.setX(this.toPixelCornerX(player.getFlag().getPos().getY()))
                .setY(this.toPixelCornerY(player.getFlag().getPos().getX()));
        }
    }

    private void performMoves() {
        movers.forEach(minion -> {
            Circle circle = this.minionToCircle.get(minion);
            circle.setX(this.toPixelCenterX(minion.getPos().getY()), Curve.LINEAR)
                .setY(this.toPixelCenterY(minion.getPos().getX()), Curve.LINEAR);
        });
    }

    public void moveMinion(Minion minion) {
        movers.add(minion);
    }
}
