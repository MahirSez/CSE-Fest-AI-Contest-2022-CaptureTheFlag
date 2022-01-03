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
import java.util.Map;

@Singleton
public class View {

    @Inject Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;
    HashMap<Minion, Sprite>minionToCircle;
    HashMap<Flag, Sprite>flagToSprite;

    List<Minion> movers;
    HashMap<Coin, Circle>coinToCircle;

    World world;
    Sprite background;
    Text leftScore, rightScore;

    List<Sprite> walls;
    int wallWidth, wallHeight;

    final String theme = "tank";

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


                // calculating player sprite scaling factor
                // Sprite minionImageSprite = graphicEntityModule.createSprite().setImage("playerShip1_red.png");
                // int targetHeight = (int) (this.wallHeight*0.8 /2);
                // double currentHeight = minionImageSprite.getBaseHeight();
                // double scalingRatio = (targetHeight - currentHeight) / currentHeight;


                Sprite minionSprite = graphicEntityModule.createSprite()    
                    .setBaseHeight((int) (this.wallHeight))
                    .setBaseWidth((int) (this.wallHeight))
                    .setAnchor(0.5)
                    .setX(this.toPixelCenterX(coord.getY()))
                    .setY(this.toPixelCenterY(coord.getX()));
                // Circle circle = graphicEntityModule.createCircle()
                //         .setRadius( (int) (this.wallHeight*0.8 /2))
                //         .setLineWidth(0)
                //         .setX(this.toPixelCenterX(coord.getY()))
                //         .setY(this.toPixelCenterY(coord.getX()))
                //         .setLineColor(0)
                //         .setLineWidth(2);

                // if(player.isLeftPlayer()) circle.setFillColor(Config.LEFT_PLAYER_COLOR);
                // else circle.setFillColor(Config.RIGHT_PLAYER_COLOR);
                if(player.isLeftPlayer()) {
                    minionSprite.setImage(theme + "/player1.png");
                    minionSprite.setRotation(Math.PI / 2);
                }
                else {
                    minionSprite.setImage(theme + "/player2.png");
                    minionSprite.setRotation(-Math.PI / 2);
                }

                this.minionToCircle.put(minion, minionSprite);
             }
         }
    }

    public void drawBackground() {
        // add background image / texture
        background = graphicEntityModule.createSprite().setImage(theme + "/back.png")
            .setBaseHeight(world.getHeight())
            .setBaseWidth(world.getWidth());
    }

    public void drawMaze(int row, int col, int[][] grid) {

        for(int i = 0 ; i < row ; i++) {
            for(int j = 0 ; j < col ; j++) {
                // int x = this.toPixelCornerX(j);
                // int y = this.toPixelCornerY(i);
                int x = this.toPixelCenterX(j);
                int y = this.toPixelCenterY(i);
                Sprite cellBlock = graphicEntityModule.createSprite()
                        .setBaseHeight( (int) (wallHeight ))
                        .setBaseWidth( (int) (wallWidth))
                        .setAnchor(0.5)
                        .setX(x)
                        .setY(y);
                        // .setLineColor(0)
                        // .setLineWidth(2);
                if(grid[i][j] == 1) {
                    // cellBlock.setFillColor(Config.WALL_COLOR);
                    cellBlock.setImage(theme + "/wall" + (int) (1 + Math.random() * 1) + ".png");
                  
                    // cellBlock.setRotation(0);
                    // graphicEntityModule.commitWorldState(0);
                    // cellBlock.setRotation(Math.PI * 0.5);
                    // graphicEntityModule.commitWorldState(0.5);
                    // cellBlock.setRotation(Math.PI);
                    // graphicEntityModule.commitWorldState(1);
                }

            
                tooltips.setTooltipText(cellBlock, i + " , " + j);

                walls.add(cellBlock);
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
                    .setImage(theme + "/flag.png")
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

    private void drawCoins() {
        for (Coin coin : maze.getAvailableCoins()) {
             Circle circle = graphicEntityModule.createCircle()
                     .setRadius( (int) (this.wallHeight * 0.7 / 2))
                     .setLineWidth(0)
                     .setX(this.toPixelCenterX(coin.getPosition().getY()))
                     .setY(this.toPixelCenterY(coin.getPosition().getX()))
                     .setLineColor(0)
                     .setLineWidth(2);
             coinToCircle.put(coin, circle);
        }
    }

    private void drawScore() {
        for (Player player : gameManager.getPlayers()) {
            if (player.isLeftPlayer()) {
                leftScore = graphicEntityModule.createText("Left: " + player.getCurrentCredit())
                        .setFontFamily("Lato")
                        .setStrokeThickness(5) // Adding an outline
                        .setStrokeColor(0xffffff) // a white outline
                        .setFontSize(50)
                        .setX(10)
                        .setY(10)
                        .setFillColor(0x000000); // Setting the text color to black
            } else {
                rightScore = graphicEntityModule.createText("Right: " + player.getCurrentCredit())
                        .setFontFamily("Lato")
                        .setStrokeThickness(5) // Adding an outline
                        .setStrokeColor(0xffffff) // a white outline
                        .setFontSize(50)
                        .setX(world.getWidth()-250)
                        .setY(10)
                        .setFillColor(0x000000); // Setting the text color to black
            }
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
        this.walls = new ArrayList<>();
        this.minionToCircle = new HashMap<>();
        this.flagToSprite = new HashMap<>();
        this.coinToCircle = new HashMap<>();

        drawBackground();
        // drawOuterRectangle();
        drawMaze(row, col, grid);
        drawMinions();
        drawFlags();
        drawCoins();
        drawScore();
    }
    public void resetData() {
        movers.clear();
    }

    public void updateFrame() {
        performMoves();
        updateFlag();
        updateScore();
    }

    private void updateScore() {
        for (Player player : gameManager.getPlayers()) {
            if (player.isLeftPlayer()) {
                leftScore.setText("Left: " + player.getCurrentCredit());
            } else {
                rightScore.setText("Right: " + player.getCurrentCredit());
            }
        }
    }

    private void updateFlag() {
        for(Player player: gameManager.getPlayers()) {
            Sprite flag = this.flagToSprite.get(player.getFlag());
            flag.setX(this.toPixelCornerX(player.getFlag().getPos().getY()))
                .setY(this.toPixelCornerY(player.getFlag().getPos().getX()));
        }
    }

    public void removeCoins(ArrayList<Coin> acquiredCoins) {
        for (Coin coin : acquiredCoins) {
            coinToCircle.get(coin).setVisible(false);
            coinToCircle.remove(coin);
        }
    }

    private void performMoves() {
        movers.forEach(minion -> {
            Sprite circle = this.minionToCircle.get(minion);
            int targetX = this.toPixelCenterX(minion.getPos().getY());
            int targetY = this.toPixelCenterY(minion.getPos().getX());
            int currentX = circle.getX();
            int currentY = circle.getY();

            if(targetX > currentX) {
                circle.setRotation(Math.PI / 2);
            }
            if(targetX < currentX) {
                circle.setRotation(-Math.PI / 2);
            }
            if(targetY > currentY) {    // Y increases downwards
                circle.setRotation(-Math.PI);
            }
            if(targetY < currentY) {
                circle.setRotation(0);
            }
            
            circle.setX(this.toPixelCenterX(minion.getPos().getY()), Curve.LINEAR)
                .setY(this.toPixelCenterY(minion.getPos().getX()), Curve.LINEAR);
        });

        walls.forEach(wall -> {
            if(Math.random() > 2) { //0.5 probability of a wall rotating  
                double rotationDeltaMax = 2 * Math.PI * 0.1; // must not rotate more than 10% of full rotation at a time
                wall.setRotation(wall.getRotation());
                graphicEntityModule.commitEntityState(0, wall);
                wall.setRotation(wall.getRotation() + Math.random() * rotationDeltaMax);
                graphicEntityModule.commitEntityState(1, wall);
            }
            
        });
    }

    public void moveMinion(Minion minion) {
        movers.add(minion);
    }
}
