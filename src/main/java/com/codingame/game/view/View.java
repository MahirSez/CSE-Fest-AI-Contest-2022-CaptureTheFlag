package com.codingame.game.view;

import com.codingame.game.*;
import com.codingame.game.action.MinePower;
import com.codingame.game.action.PowerUpType;
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
    @Inject Game game;
    @Inject Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;
    HashMap<Minion, Sprite> minionToSprite;
    HashMap<Flag, Sprite>flagToSprite;
    HashMap<MinePower, Sprite>mineToSprite;


    // Note: Damaged minions also contain dead minions
    List<Minion> deadMinions, damagedMinions, frozenMinions;
    List<Minion> flamers, freezers, miners;
    List<Coord> flamedCells, frozenCells, minedCells;
    List<Minion> movers;
    List<Sprite> currentFrameSprites;
    List<Coin> removedCoins;
    List<MinePower> placedMines;
    List<MinePower> detonatedMines;


    HashMap<Coin, Sprite>coinToSprite;

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
                    .setY(this.toPixelCenterY(coord.getX()))
                    .setZIndex(3);

                if(player.isLeftPlayer()) {
                    minionSprite.setImage(theme + "/player1.png");
                    minionSprite.setRotation(Math.PI / 2);
                }
                else {
                    minionSprite.setImage(theme + "/player2.png");
                    minionSprite.setRotation(-Math.PI / 2);
                }

                this.minionToSprite.put(minion, minionSprite);
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
                    .setZIndex(5);


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
        final int COIN_DIMENSION = 564;

        for (Coin coin : game.getAvailableCoins()) {
            int x = this.toPixelCenterX(coin.getPosition().getY());
            int y = this.toPixelCenterY(coin.getPosition().getX());

             Sprite sprite = graphicEntityModule.createSprite()
                     .setScale(wallHeight/(double)COIN_DIMENSION * 0.3)
                     .setX(x)
                     .setY(y);

             if (coin.getValue() == Config.COIN_VALUES[0]) {
                 sprite.setImage("coin/Bronze.png");
             } else if (coin.getValue() == Config.COIN_VALUES[1]) {
                 sprite.setImage("coin/Silver.png");
             } else if (coin.getValue() == Config.COIN_VALUES[2]) {
                 sprite.setImage("coin/Gold.png");
             }

             coinToSprite.put(coin, sprite);
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
        this.deadMinions = new ArrayList<>();
        this.flamers = new ArrayList<>();
        this.freezers = new ArrayList<>();
        this.miners = new ArrayList<>();
        this.damagedMinions = new ArrayList<>();
        this.frozenMinions = new ArrayList<>();
        this.currentFrameSprites = new ArrayList<>();
        this.removedCoins = new ArrayList<>();
        this.placedMines = new ArrayList<>();
        this.detonatedMines = new ArrayList<>();


        this.flamedCells = new ArrayList<>();
        this.frozenCells = new ArrayList<>();
        this.minedCells = new ArrayList<>();

        this.walls = new ArrayList<>();
        this.minionToSprite = new HashMap<>();
        this.flagToSprite = new HashMap<>();
        this.coinToSprite = new HashMap<>();
        this.mineToSprite = new HashMap<>();

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
        deadMinions.clear();
        flamers.clear();
        freezers.clear();
        miners.clear();
        damagedMinions.clear();
        frozenMinions.clear();
        removedCoins.clear();
        placedMines.clear();
        detonatedMines.clear();

        flamedCells.clear();
        frozenCells.clear();
        minedCells.clear();

        for(Sprite sprite: currentFrameSprites) {
            sprite.setVisible(false);
        }
        currentFrameSprites.clear();


    }

    private void generatePowerUpSprites(List<Coord> affectedCells, String imageFile) {

        for(Coord coord : affectedCells) {

            int x = this.toPixelCornerX(coord.getY());
            int y = this.toPixelCornerY(coord.getX());
            Sprite sprite = graphicEntityModule.createSprite()
                    .setBaseHeight((int) (this.wallHeight * 0.9))
                    .setBaseWidth((int) (this.wallHeight * 0.9))
                    .setX(x)
                    .setY(y)
                    .setAnchorX(-0.2)
                    .setImage(imageFile)
                    .setZIndex(4);
            currentFrameSprites.add(sprite);
        }
    }


    private void showDetonatedMines() {
        for(MinePower mine: detonatedMines) {
            mineToSprite.get(mine).setVisible(false);
        }
        generatePowerUpSprites(minedCells, "powerups/mine_explosion.png");
    }

    private void showFirePower() {
        generatePowerUpSprites(flamedCells, "powerups/flame.png");
    }

    private void showFreezePower() {
        generatePowerUpSprites(frozenCells, "powerups/freeze.png");
    }


    private void showPowerUpsInCells() {
        showFirePower();
        showFreezePower();
        showDetonatedMines();
    }

    private void removeDeadMinions() {
        for(Minion minion: this.deadMinions) {
            minionToSprite.get(minion).setVisible(false);
        }
    }

    public void updateFrame() {
        removeDeadMinions();
        removeCoins();
        performMoves();
        addMines();
        showPowerUpsInCells();
        updateFlag();
        updateScore();
    }

    private void addMines() {
        for(MinePower mine: placedMines) {
            int x = this.toPixelCornerX(mine.getOrigin().getY());
            int y = this.toPixelCornerY(mine.getOrigin().getX());
            Sprite sprite = graphicEntityModule.createSprite()
                    .setBaseHeight((int) (this.wallHeight))
                    .setBaseWidth((int) (this.wallHeight))
                    .setX(x)
                    .setY(y)
                    .setAnchorX(-0.2)
                    .setImage("powerups/mine.png");

            mineToSprite.put(mine, sprite);
        }
    }

    private void removeCoins() {
        for (Coin coin : this.removedCoins) {
            coinToSprite.get(coin).setVisible(false);
            coinToSprite.remove(coin);
        }
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
        this.removedCoins.addAll(acquiredCoins);
    }

    private void performMoves() {
        movers.forEach(minion -> {
            Sprite circle = this.minionToSprite.get(minion);
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

    public void addDeadMinion(Minion minion) {
        deadMinions.add(minion);
    }

    public void addPowerUpUser(Minion minion, PowerUpType power) {
        switch (power) {
            case FIRE:
                flamers.add(minion);
                break;
            case MINE:
                miners.add(minion);
                break;
            case FREEZE:
                freezers.add(minion);
        }
    }



    public void addAffectedMinions(List<Minion> affectedMinions, PowerUpType power) {
        switch (power) {
            case FIRE:
            case MINE:
                this.damagedMinions.addAll(affectedMinions);
                break;
            case FREEZE:
                this.frozenMinions.addAll(affectedMinions);
        }
    }

    public void addMine(MinePower mine) {
        this.placedMines.add(mine);
    }

    public void addDetonatedMine(MinePower mine) {
        this.detonatedMines.add(mine);
    }

    public void addAffectedCells(List<Coord> affectedCells, PowerUpType power) {
        switch (power) {
            case FIRE:
                this.flamedCells.addAll(affectedCells);
                break;
            case MINE:
                this.minedCells.addAll(affectedCells);
                break;
            case FREEZE:
                this.frozenCells.addAll(affectedCells);
                break;
        }
    }

}
