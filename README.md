# OOS-src.SnakeGame


Guidelines to install and run our game

1. external Libraries
- LWJGL : Library for Main GUI used in this project "SnakeGame"
  https://www.lwjgl.org/download
- Set opengl.jar, glfw.jar, macros.jar, stb.jar from LWJGL library to dependency
- Joml 1.8.2 : Matrix3D, Matrix4D -> Need for rendering each components -> https://github.com/JOML-CI/JOML/releases
  ● Must be 1.8.2 ver!

2. internal files
- user.acc : acc file to save the entire gameboard -> Need user.acc file in project repository to use "load option"
- rank.acc : acc file to record previous plays' score -> Every time program starts, it automatically loads ranking from rank.acc. If there's no data, it produces new ranking.
- imgfolder : Contains all png files for rendering in the game. It basically locate at git repository, but snakeGame/imgfolder's path should be maintained so renderer can detect png file.

3. Game Play


1) Main Menu
- Executed by clicking the buttons
- Single play : Snake Game Single mode starts.
- Dual Play : Snake Game Dual mode starts.
- Auto Play : Snake Game Auto mode starts.
- load : Loads the Game file saved in user.acc.
- ranking : Recalls the locally played game's Ranking information saved in rank.acc.
- exit : Exit the game.

2) Game Start
- Arrow Key : Change the snake's direction
- esc & p : Recalls pause menu.

3) Pause Menu
- resume : Continues the game -> Restarts the game from the paused moment.
- restart : Initialize all information includes score, fruitPosition, snake and restarts the game from beginning.
- save : Save the current state of the game.
- exit : Initialize all information and Returns to Main Menu.

Class Explanation
-
1. SnakeBody
- One cell that compose the Snake in game
- Contains the Snake's position data, and subjected to rendering.
- positionX, positionY(float) : coordinate of current Snake Body
- method : changeDirection, move, getter, movePosition(specific position)

2. Snake
- Snake file itself
- Composed of list of SnakeBody, controls the SnakeBody through method. 
- method : grow, change_Direction, move, check_If_Collapse, check_If_Overlap, re_Init

3. GameBoard
- Class that controls all operation that should be done while SnakeGame processes.
- Controlling Snake, producing Fruit, renewaling score, managing Ranking, save & load
- method : loadGame, createFruit, move_Snake, change_Direction_Snake, setNickname, check_fruit_overlap, save_this_game, loadGame

4. Controller
- Class that mediates the interaction of gameBoard, renderer, and LWGJL window.
- Defines the keyboard Event handler
- loop : practical operation
