package com.codingame.game.view;

import com.codingame.game.*;
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

    @Inject Maze maze;
    @Inject GraphicEntityModule graphicEntityModule;
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject TooltipModule tooltips;
    HashMap<Minion, Sprite> minionToSprite;
    HashMap<Flag, Sprite>flagToSprite;


    // Note: Damaged minions also contain dead minions
    List<Minion> deadMinions, damagedMinions, frozenMinions;
    List<Minion> flamers, freezers, miners;
    List<Minion> movers;
    List<Sprite> currentFrameSprites;


    HashMap<Coin, Sprite>coinToSprite;

    World world;
    Sprite background;
    BitmapText leftScore, rightScore;
    List<BitmapText> scoreLabels;

    List<Sprite> walls;
    int wallWidth, wallHeight;
    int emptyPixelX, emptyPixelY;

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
        return x * wallWidth + this.emptyPixelX / 2;
    }
    int toPixelCornerY(int y) {
        return Config.MAZE_UPPER_OFFSET + wallHeight * y + this.emptyPixelY;
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

                this.minionToSprite.put(minion, minionSprite);
             }
         }
    }

    public void drawBackground() {
        // add background image / texture
        // TODO: change background image
        background = graphicEntityModule.createSprite().setImage(theme + "/back2.png")
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
                } else {
                    cellBlock.setImage(theme + "/back.png");
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
        final int COIN_DIMENSION = 564;

        for (Coin coin : maze.getAvailableCoins()) {
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

    private void drawHud() {
        // TODO: change HUD design
        int hudWidth = 530;
        Group hudGroup = graphicEntityModule.createGroup()
                .setZIndex(10);

        Sprite hudRed = graphicEntityModule.createSprite()
                .setImage("HUD_Masque_RED")
                .setZIndex(1);

        Sprite hudBlue = graphicEntityModule.createSprite()
                .setImage("HUD_Masque_BLUE")
                .setX(world.getWidth() - hudWidth)
                .setZIndex(1);

        hudGroup.add(hudRed, hudBlue);

        scoreLabels = new ArrayList<BitmapText>(gameManager.getPlayerCount());

        int playerHudZoneWidth = world.getWidth() / (gameManager.getPlayerCount());
        double avatarRotation = 0.08;
        int avatarSize = 130;
        int playerHudNameOffset = 630;
        int playerHudScoreOffset = 320;
        int playerHudAvatarOffset = 882;

        for (Player p : gameManager.getPlayers()) {

            int coefMirror = p.getIndex() == 0 ? -1 : 1;

            BitmapText nameLabel = graphicEntityModule.createBitmapText()
                    .setFont("BRLNS_66")
                    .setFontSize(36)
                    .setText(p.getNicknameToken())
                    .setMaxWidth(300)
                    .setAnchorX(0.5)
                    .setX(playerHudZoneWidth + coefMirror * playerHudNameOffset)
                    .setY(7)
                    .setZIndex(2);

            BitmapText scoreLabel = graphicEntityModule.createBitmapText()
                    .setFont("BRLNS_66")
                    .setFontSize(72)
                    .setText("0")
                    .setAnchorX(0.5)
                    .setX(playerHudZoneWidth + coefMirror * playerHudScoreOffset)
                    .setY(10)
                    .setZIndex(2);

            Sprite avatar = graphicEntityModule.createSprite()
                    .setImage(p.getAvatarToken())
                    .setAnchor(0.5)
                    .setX(playerHudZoneWidth + coefMirror * playerHudAvatarOffset)
                    .setY(70)
                    .setRotation(coefMirror * avatarRotation)
                    .setBaseHeight(avatarSize)
                    .setBaseWidth(avatarSize)
                    .setZIndex(0);

            hudGroup.add(nameLabel, scoreLabel, avatar);
            scoreLabels.add(scoreLabel);
        }
    }

    public void init() {
        this.world = graphicEntityModule.getWorld();


        int[][] grid = maze.getGrid();
        int row = grid.length;
        int col = grid[0].length;

        this.wallWidth = world.getWidth() / col;
        this.wallHeight = (world.getHeight() - Config.MAZE_UPPER_OFFSET) / row;

        // because of integer division there will remain empty pixels
        this.emptyPixelX = world.getWidth() - col * this.wallWidth;
        this.emptyPixelY = (world.getHeight() - Config.MAZE_UPPER_OFFSET) - row * this.wallHeight;

        this.movers = new ArrayList<>();
        this.deadMinions = new ArrayList<>();
        this.flamers = new ArrayList<>();
        this.freezers = new ArrayList<>();
        this.miners = new ArrayList<>();
        this.damagedMinions = new ArrayList<>();
        this.frozenMinions = new ArrayList<>();
        this.currentFrameSprites = new ArrayList<>();

        this.walls = new ArrayList<>();
        this.minionToSprite = new HashMap<>();
        this.flagToSprite = new HashMap<>();
        this.coinToSprite = new HashMap<>();

        drawBackground();
        // drawOuterRectangle();
        drawMaze(row, col, grid);
        drawMinions();
        drawFlags();
        drawCoins();
        drawHud();
    }
    public void resetData() {
        movers.clear();
        deadMinions.clear();
        flamers.clear();
        freezers.clear();
        miners.clear();
        damagedMinions.clear();
        frozenMinions.clear();
        for(Sprite sprite: currentFrameSprites) {
            sprite.setVisible(false);
        }
        currentFrameSprites.clear();
    }

    private void showPowerUps() {
        showFirePower();
        showFreezePower();
    }



    private ArrayList<Coord>getAffectedCells(Coord origin) {

        ArrayList<Coord>affectedCoords = new ArrayList<>();
        affectedCoords.add(origin);

        int x = origin.getX();
        int y = origin.getY();

        int[] dx = {1 ,-1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for(int k = 0 ; k < 4 ; k++) {
            for(int scale = 1 ; ; scale++) {
                int xx = x + dx[k] * scale;
                int yy = y + dy[k] * scale;
                if(xx < 0 || xx >= maze.getRow() || yy < 0 || yy >= maze.getCol() || maze.getGrid()[xx][yy] == 1) {
                    break;
                }
                affectedCoords.add(new Coord(xx, yy));
            }
        }
        return affectedCoords;
    }

    private void generatePowerUpSprites(List<Minion> minions, String imageFile) {

        for(Minion minion: minions) {
            Coord origin = minion.getPos();

            for(Coord coord : this.getAffectedCells(origin)) {

                int x = this.toPixelCornerX(coord.getY());
                int y = this.toPixelCornerY(coord.getX());
                Sprite sprite = graphicEntityModule.createSprite()
                        .setBaseHeight((int) (this.wallHeight * 0.9))
                        .setBaseWidth((int) (this.wallHeight * 0.9))
                        .setX(x)
                        .setY(y)
                        .setAnchorX(-0.2)
                        .setImage(imageFile);
                currentFrameSprites.add(sprite);
            }
        }
    }

    private void showFirePower() {
        generatePowerUpSprites(flamers, "powerups/flame.png");
    }

    private void showFreezePower() {
        generatePowerUpSprites(freezers, "powerups/freeze.png");
    }

    private void removeDeadMinions() {
        for(Minion minion: this.deadMinions) {
            minionToSprite.get(minion).setVisible(false);
        }
    }

    public void updateFrame() {
        removeDeadMinions();
        performMoves();
        showPowerUps();
        updateFlag();
        updateScores();
    }


    public void updateScores() {
        for (Player p : gameManager.getPlayers()) {
            BitmapText scoreLabel = scoreLabels.get(p.getIndex());
            scoreLabel.setText(String.valueOf(p.getCurrentCredit()));
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
            coinToSprite.get(coin).setVisible(false);
            coinToSprite.remove(coin);
        }
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

    public void addDamagedMinions(List<Minion> damagedMinions) {
        this.damagedMinions.addAll(damagedMinions);
    }

    private void addFrozenMinions(List<Minion> frozenMinions) {
        this.frozenMinions.addAll(frozenMinions);
    }


    public void addAffectedMinions(List<Minion> affectedMinions, PowerUpType power) {
        switch (power) {
            case FIRE:
            case MINE:
                addDamagedMinions(affectedMinions);
                break;
            case FREEZE:
                addFrozenMinions(affectedMinions);
        }
    }

}
