package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.World;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class View {

    @Inject Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;


    World world;

    int wallWidth, wallHeight;


    void drawOuterRectangle() {
        graphicEntityModule.createRectangle()
                .setHeight(world.getHeight() - Config.MAZE_UPPER_OFFSET)
                .setWidth(world.getWidth())
                .setX(0)
                .setY(Config.MAZE_UPPER_OFFSET);
    }

    void drawMinions() {
         for(Player player: gameManager.getPlayers()) {
             for(Minion minions: player.getMinions()) {
                Coord coord = minions.getPos();
                Circle circle = graphicEntityModule.createCircle()
                        .setRadius(20)
                        .setLineWidth(0)
                        .setX(coord.y * this.wallWidth + this.wallWidth / 2)
                        .setY(Config.MAZE_UPPER_OFFSET + coord.x * this.wallHeight + this.wallHeight / 2);

                if(player.getIsLeftPlayer()) circle.setFillColor(0x00FF00);
                else circle.setFillColor(0x0000FF);

             }
         }
    }

    void drawBackground() {
        // add background image / texture
    }

    void drawMaze(int row, int col, int[][] grid) {

        for(int i = 0 ; i < row ; i++) {
            for(int j = 0 ; j < col ; j++) {
                int x = wallWidth * j;
                int y = Config.MAZE_UPPER_OFFSET + wallHeight * i;

                Rectangle wallBlock = graphicEntityModule.createRectangle()
                        .setHeight(wallHeight)
                        .setWidth(wallWidth)
                        .setX(x)
                        .setY(y)
                        .setLineColor(0)
                        .setLineWidth(2);
                if(grid[i][j] == 1) {
                    wallBlock.setFillColor(0x964B00);
                }
                tooltips.setTooltipText(wallBlock, i + " , " + j);
            }
        }
    }

    void init() {
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
    }
}
