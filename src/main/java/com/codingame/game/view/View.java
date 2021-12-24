package com.codingame.game.view;

import com.codingame.game.*;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class View {

    @Inject
    Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;

//    List<Move>movers;

    World world;

    int wallWidth, wallHeight;

    public void drawOuterRectangle() {
        graphicEntityModule.createRectangle()
                .setHeight(world.getHeight() - Config.MAZE_UPPER_OFFSET)
                .setWidth(world.getWidth())
                .setX(0)
                .setY(Config.MAZE_UPPER_OFFSET);
    }

    public void drawMinions() {
         for(Player player: gameManager.getPlayers()) {
             for(Minion minions: player.getMinions()) {
                Coord coord = minions.getPos();
                Circle circle = graphicEntityModule.createCircle()
                        .setRadius(20)
                        .setLineWidth(0)
                        .setX(coord.getY() * this.wallWidth + this.wallWidth / 2)
                        .setY(Config.MAZE_UPPER_OFFSET + coord.getX() * this.wallHeight + this.wallHeight / 2)
                        .setLineColor(0)
                        .setLineWidth(2);

                if(player.isLeftPlayer()) circle.setFillColor(Config.LEFT_PLAYER_COLOR);
                else circle.setFillColor(Config.RIGHT_PLAYER_COLOR);

             }
         }
    }

    public void drawBackground() {
        // add background image / texture
    }

    public void drawMaze(int row, int col, int[][] grid) {

        for(int i = 0 ; i < row ; i++) {
            for(int j = 0 ; j < col ; j++) {
                int x = wallWidth * j;
                int y = Config.MAZE_UPPER_OFFSET + wallHeight * i;

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

            int x = wallWidth * player.getFlag().getPos().getY();
            int y = Config.MAZE_UPPER_OFFSET + wallHeight * player.getFlag().getPos().getX();;


            Rectangle cellBlock = graphicEntityModule.createRectangle()
                    .setHeight(wallHeight)
                    .setWidth(wallWidth)
                    .setX(x)
                    .setY(y);

            Sprite flag = graphicEntityModule.createSprite()
                    .setImage("flag.png")
                    .setBaseWidth(this.wallWidth)
                    .setBaseHeight(this.wallHeight)
                    .setX(x)
                    .setY(y);


            if(player.isLeftPlayer()) {
                flag.setTint(Config.LEFT_PLAYER_COLOR);
                cellBlock.setLineColor(Config.LEFT_PLAYER_COLOR);
            }
            else {
                flag.setTint(Config.RIGHT_PLAYER_COLOR);
                cellBlock.setLineColor(Config.RIGHT_PLAYER_COLOR);
            }
            cellBlock.setLineWidth(7);
        }
    }

    public void init() {
        this.world = graphicEntityModule.getWorld();


        int[][] grid = maze.getGrid();
        int row = grid.length;
        int col = grid[0].length;

        this.wallWidth = world.getWidth() / col;
        this.wallHeight = (world.getHeight() - Config.MAZE_UPPER_OFFSET) / row;

        drawBackground();
        drawOuterRectangle();
        drawMaze(row, col, grid);
        drawMinions();
        drawFlags();
    }

    public void updateFrame() {

    }

    public void moveMinion(Minion minion, Coord pos, Coord coord) {

    }
}
