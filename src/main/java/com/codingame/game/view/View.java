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
    HashMap<Coord, Sprite>cellToSprite;


    // Note: Damaged minions also contain dead minions
    List<Minion> deadMinions, damagedMinions, frozenMinions;
    List<Minion> flamers, freezers, miners;
    List<Coord> flamedCells, frozenCells, minedCells;
    List<Minion> movers;
    List<Sprite> currentFrameSprites;
    List<Coin> removedCoins;
    List<MinePower> placedMines;
    List<MinePower> detonatedMines;

    // animation state variables

    final int STARTED = 0;
    final int ONGOING = 1;
    final int STOPPED = -1;
    int fireAnimState = STOPPED;

    List<Sprite> firesLX = null;
    List<Sprite> firesRX = null;
    List<Sprite> firesTY = null;
    List<Sprite> firesBY = null;


    HashMap<Coin, Sprite>coinToSprite;

    World world;
    TilingSprite background;
    BitmapText leftScore, rightScore;
    List<BitmapText> scoreLabels;

    List<Sprite> walls;
    int wallWidth, wallHeight;
    int emptyPixelX, emptyPixelY;

    final String theme = "tank";
    final int numWalls = 1;
    final float wallScaling = 0.8f;
    final float minionScaling = 1.5f;
    final int wallFillerOffset = 1;

    // firing params
    final double fireSpriteScaleFactor = 30;

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
                    .setBaseHeight((int) ((this.wallHeight) * 1.5 *  minionScaling))
                    .setBaseWidth((int) ((this.wallHeight) * minionScaling))
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
        // TODO: change background image
        background = graphicEntityModule.createTilingSprite().setImage(theme + "/back2.png")
            .setBaseHeight(world.getHeight())
            .setBaseWidth(world.getWidth());
    }

    public void drawMaze(int row, int col, int[][] grid) {

        for(int i = 0 ; i < row ; i++) {
            for(int j = 0 ; j < col ; j++) {

                int x = this.toPixelCenterX(j);
                int y = this.toPixelCenterY(i);
                Sprite cellBlock = graphicEntityModule.createSprite()
                        .setBaseHeight( (int) (wallHeight * wallScaling))
                        .setBaseWidth( (int) (wallWidth * wallScaling))
                        .setAnchor(0.5)
                        .setX(x)
                        .setY(y);


                if(grid[i][j] == 1) {

                    cellBlock.setImage(theme + "/wall" + (int) (1 + Math.random() * (numWalls)) + ".png");
                    
                    
                    if(i + 1 < row && grid[i + 1][j] == 1) {
                        // wall to right
                        Sprite filler = graphicEntityModule.createSprite()
                            .setBaseHeight( (int) (wallHeight * 0.5))
                            .setBaseWidth( (int) (wallWidth * 0.5))
                            .setAnchorY(0.4)
                            .setAnchorX(0.5)
                            .setX(x + wallFillerOffset)
                            .setY(y)
                            .setImage(theme + "/wall" + (int) (1 + Math.random() * (numWalls)) + ".png");
                    }

                    if(j + 1 < col && grid[i][j + 1] == 1) {
                        Sprite filler = graphicEntityModule.createSprite()
                            .setBaseHeight( (int) (wallHeight * 0.5))
                            .setBaseWidth( (int) (wallWidth * 0.5))
                            .setAnchorY(0.5)
                            .setAnchorX(0.4)
                            .setX(x)
                            .setY(y + wallFillerOffset)
                            .setImage(theme + "/wall" + (int) (1 + Math.random() * (numWalls)) + ".png");
                    }
                   
                } else {
                    // cellBlock.setImage(theme + "/back.png");
                }

                cellToSprite.put(new Coord(i, j), cellBlock);
                tooltips.setTooltipText(cellBlock, i + " , " + j);
                walls.add(cellBlock);
            }
        }
    }

    private void drawFlags() {
        for(Player player: gameManager.getPlayers()) {

            Flag flag = player.getFlag();
            int x = this.toPixelCornerX(flag.getPos().getY());
            int y = this.toPixelCornerY(flag.getPos().getX());

            Sprite flagSprite = graphicEntityModule.createSprite()
                    .setImage(theme + "/flag.png")
                    .setBaseWidth(this.wallWidth)
                    .setBaseHeight(this.wallHeight)
                    .setX(x)
                    .setY(y)
                    .setZIndex(5);

            cellToSprite.get(flag.getPos())
                    .setImage(theme + String.format("/flag_base_%s.png",player.getColor()));



            if(player.isLeftPlayer()) {
                flagSprite.setTint(Config.LEFT_PLAYER_COLOR);
            }
            else {
                flagSprite.setTint(Config.RIGHT_PLAYER_COLOR);
            }
            flagToSprite.put(player.getFlag(), flagSprite);
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

        for (Player player : gameManager.getPlayers()) {

            int coefMirror = player.getIndex() == 0 ? -1 : 1;
            int color = player.isLeftPlayer() ? Config.LEFT_PLAYER_COLOR: Config.RIGHT_PLAYER_COLOR;

            BitmapText nameLabel = graphicEntityModule.createBitmapText()
                    .setFont("BRLNS_66")
                    .setFontSize(36)
                    .setText(player.getNicknameToken())
                    .setMaxWidth(300)
                    .setAnchorX(0.5)
                    .setX(playerHudZoneWidth + coefMirror * playerHudNameOffset)
                    .setY(7)
                    .setZIndex(2);

            BitmapText scoreLabel = graphicEntityModule.createBitmapText()
                    .setFont("BRLNS_66")
                    .setFontSize(72)
                    .setText(player.getCurrentCredit()+"")
                    .setAnchorX(0.5)
                    .setX(playerHudZoneWidth + coefMirror * playerHudScoreOffset)
                    .setY(10)
                    .setZIndex(2)
                    .setTint(color);

            Sprite avatar = graphicEntityModule.createSprite()
                    .setImage(player.getAvatarToken())
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

        this.wallWidth = (int) ((world.getWidth() / col))  ;
        this.wallHeight = (int) (((world.getHeight() - Config.MAZE_UPPER_OFFSET) / row));

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
        this.removedCoins = new ArrayList<>();
        this.placedMines = new ArrayList<>();
        this.detonatedMines = new ArrayList<>();


        this.flamedCells = new ArrayList<>();
        this.frozenCells = new ArrayList<>();
        this.minedCells = new ArrayList<>();

        this.walls = new ArrayList<>();
        this.cellToSprite = new HashMap<>();
        this.minionToSprite = new HashMap<>();
        this.flagToSprite = new HashMap<>();
        this.coinToSprite = new HashMap<>();
        this.mineToSprite = new HashMap<>();

        firesLX = new ArrayList<>();
        firesRX = new ArrayList<>();
        firesTY = new ArrayList<>();
        firesBY = new ArrayList<>();

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

        // fireAnimState = STOPPED;



    }

    private void generatePowerUpSprites(List<Coord> affectedCells, String imageFile) {

        // int width, height;

        if(fireAnimState == STARTED) {

            if(affectedCells.isEmpty()) {
                fireAnimState = STOPPED;
                
                firesRX.clear();
                firesLX.clear();
                firesTY.clear();
                firesBY.clear();
            }
            firesRX.forEach(fire -> {
                if(fire.getScaleX() < 1) {
                    fire.setScaleX(fire.getScaleX() * fireSpriteScaleFactor);
                }
            });
            firesLX.forEach(fire -> {
                if(fire.getScaleX() > -1) {
                    fire.setScaleX(fire.getScaleX() * fireSpriteScaleFactor);
                }
            });
            firesTY.forEach(fire -> {
                if(fire.getScaleY() < 1) {
                    fire.setScaleY(fire.getScaleY() * fireSpriteScaleFactor);
                }   
            });
            firesBY.forEach(fire -> {
                if(fire.getScaleY() > -1) {
                    fire.setScaleY(fire.getScaleY() * fireSpriteScaleFactor);
                }   
            });

            
        }
        if(affectedCells.size() > 0) {

            
            System.err.println(fireAnimState);


        
            if(fireAnimState == STOPPED) {
                fireAnimState = STARTED;
               



                // flamers coords are the origins of the fire effect
                freezers.forEach(flamer -> {
                    //2 sprites per flamer
                    int originX = this.toPixelCornerX(flamer.getPos().getY());
                    int originY = this.toPixelCornerY(flamer.getPos().getX());

                    // find bounds per flamer
                    int l = 99999;
                    int r = 0;
                    int t = 0;
                    int b = 99999;
                    for(Coord coord : affectedCells) {
                        int x = this.toPixelCornerX(coord.getY());
                        int y = this.toPixelCornerY(coord.getX());
                        if (x == originX) {
                            if (y < b) b = y;
                            if (y > t) t = y;  
                        }
                        if (y == originY) {
                            if (x < l) l = x;
                            if (x > r) r = x;  
                        }
                    }
                    int lwidth = Math.abs(l - originX);
                    int rwidth = Math.abs(r - originX);
                    // int width = Math.abs(l - r);
                    int theight = Math.abs(t - originY);
                    int bheight = Math.abs(b - originY);
    
                    // System.out.println("width " + width);

                    Sprite fireRX = graphicEntityModule.createSprite()
                    .setBaseHeight((int) (wallHeight * 0.5))
                    .setBaseWidth((int) rwidth)
                    .setX(originX)
                    .setY(originY)
                    .setScaleX(0.001)
                    .setAnchor(0)
                    .setImage(imageFile)
                    .setZIndex(4);

                    Sprite fireLX = graphicEntityModule.createSprite()
                    .setBaseHeight((int) (wallHeight * 0.5))
                    .setBaseWidth((int) lwidth)
                    .setX(originX)
                    .setY(originY)
                    .setScaleX(-0.001)
                    .setAnchor(0)
                    .setImage(imageFile)
                    .setZIndex(4);

                    Sprite fireTY = graphicEntityModule.createSprite()
                    .setBaseWidth((int) (wallWidth * 0.5))
                    .setBaseHeight((int) theight)
                    .setX(originX)
                    .setY(originY)
                    .setScaleY(0.001)
                    .setAnchor(0)
                    .setImage(imageFile)
                    .setZIndex(4);

                    Sprite fireBY = graphicEntityModule.createSprite()
                    .setBaseWidth((int) (wallWidth * 0.5))
                    .setBaseHeight((int) bheight)
                    .setX(originX)
                    .setY(originY)
                    .setScaleY(-0.001)
                    .setAnchor(0)
                    .setImage(imageFile)
                    .setZIndex(4);
    
                    firesRX.add(fireRX);

                    firesLX.add(fireLX);

                    firesTY.add(fireTY);

                    firesBY.add(fireBY);

                    graphicEntityModule.commitEntityState(0, fireRX, fireLX, fireTY, fireBY);

                    fireRX.setScaleX(1);
                    fireLX.setScaleX(-1);
                    fireTY.setScaleY(1);
                    fireBY.setScaleY(-1);

                    graphicEntityModule.commitEntityState(1, fireRX, fireLX, fireTY, fireBY);
                    
                });    

                currentFrameSprites.addAll(firesBY);
                currentFrameSprites.addAll(firesTY);
                currentFrameSprites.addAll(firesRX);
                currentFrameSprites.addAll(firesLX);
            }
            
        }
            // for(Coord coord : affectedCells) {

            //     int x = this.toPixelCornerX(coord.getY());
            //     int y = this.toPixelCornerY(coord.getX());
            //     Sprite sprite = graphicEntityModule.createSprite()
            //             .setBaseHeight((int) (this.wallHeight * 0.9))
            //             .setBaseWidth((int) (this.wallHeight * 0.9))
            //             .setX(x)
            //             .setY(y)
            //             .setAnchorX(-0.2)
            //             .setImage(imageFile)
            //             .setZIndex(4);
            //     currentFrameSprites.add(sprite);
            // }
        // }
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
        updateScores();
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
