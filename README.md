# Documentation

Game Link: [https://www.codingame.com/contribute/view/7845e540d4efbbe4d9f4b68686460bb53424](https://www.codingame.com/ide/demo/9421946f2e2ee3b64f9d10ff8e195b67d51dff)


Useful Resources
-----------------

1. Getting Started with multiplayer game: https://www.codingame.com/playgrounds/25775/codingame-sdk-documentation/create-a-multiplayer-game
2. javadoc: https://codingame.github.io/codingame-game-engine/
3. Front-end Assets: https://github.com/CodinGame/codingame-sdk-assets
4. Pacman Game: https://www.codingame.com/ide/puzzle/spring-challenge-2020
5. Pacman Game GitHub: https://github.com/CodinGame/SpringChallenge2020 


Player's I/O
-------------

Line 1: two integers `height` and `width` - the size of the maze. <br>
Next `height` lines: a string of `width` characters each representing one cell of a row: `'.'` is an empty cell and `'#'` is a wall. <br>
Next line: `my_flag_base_x` & `my_flag_base_y` - player's flag-base position <br>
Next line: `opponent_flag_base_x` & `opponent_flag_base_y` - opponent's flag-base position


Input for each game turn: <br>
Line 1: `my_score` & `opponent_score` - player's and opponent's current score <br>
Line 2: `my_flag_pos_x`, `my_flag_pos_y` & `my_flag_captured` - player's flag position and whether it is currently being carried by opponet's minion (1 if captured, 0 otherwise) <br>
Line 3: `opponent_flag_pos_x`, `opponent_flag_pos_y` & `opponent_flag_captured` - opponent's flag position and whether it is currently being carried by player's minion (1 if captured, 0 otherwise) <br>
Line 4: `my_alive_minion_cnt` - number of player's alive minions <br>
Next `my_alive_minion_cnt` lines: `id` `pos_x` `pos_y` `health` - player's minion's id, position & health <br>
Next Line: `visible_minion_cnt` - number of opponent's visible minions <br>
Next `visible_minion_cnt` lines: `id` `pos_x` `pos_y` `health` - opponent's minion's id, position & health <br>



High-level Code calling Sequence
---------------------------------

1. `Referee.init()` : All the backend and frontend based initializations are done here. 
    1. `maze.init()`: The maze is generated
    2. `game.init()`: All the minions, flag and flag base along with their positions are generated
    3. `view.init()`: The initial frontend-view is generated like drawing the  background, maze and the initial view of minions and flags
    4. `this.sendInitialInfo()`: Send all the players global info about the game before entering the gameloop

2. `Referr.gameTurn(int turn)`: Called on each game loop. `turn` variable is not used as our game is not turn based like tic-tac-toe.
    1. `game.resetTurnData()`: Resets data on each turn.
    2. `sendGameStateToPlayers()`: Send each player current game state. (see player's i/o) and executes player's code. 
    3. `handlePlayerCommands()`: Parses player's output and sets the intended action to the corresponding minion. 
    4. `view.resetData()`: reset any view-based data
    5. `game.updateGameState()`: Updates game state: changes minions' movement, flag's position and prints game summary
    6. `view.updateFrame()`: changes minions' and flags' position in the front-end
    7. `game.endGame()`: adds winner's name to game-summary
    8. `gameManager.endGame()`: ends the game and stops further i/o and frame update.


Class Descriptions
---------------------

1. CommandParser: Parses Player's commands
2. Config: Contains game parameters
3. Coord: Maze Co-ordinate class
4. Flag
5. FlagBase
6. Game: Class for performing different game operations
7. Maze
8. Minion
9. Player
10. RandomUtil: Utility class for generating random numbers
11. Referee: Handles the game (Mediator class)


Packages
--------
1. action: contains different types of minion actions
2. exception
3. grid: [ Copied from pacman game ]
4. view: contains Front-end stuffs





    
