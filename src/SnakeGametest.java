/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src;

import src.board.Board;
import src.controller.LWJGL_Controller2;
import src.controller.MainController;
import src.domain.GameBoard;
import src.domain.Snake;
import src.entity.Entity;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

public class SnakeGametest {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static final int BOARD_WIDTH = 42;
    public static final int BOARD_HEIGHT = 42;
    public static final int BOARD_SCALE = 16;
    public static final float SNAKE_SPEED = 0.3f;

    public static void main(String[] args) {
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        Shader shader = new Shader("shader");
        Camera cam = new Camera(mainwindow.getWidth(), mainwindow.getHeight());
        Board mainboard = new Board(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SCALE);
        Entity snake = new Entity((float) BOARD_WIDTH, -(float) BOARD_HEIGHT, SNAKE_SPEED);
        MainController controller = new MainController();
        Snake snake2 = new Snake();
        GameBoard gb = new GameBoard(snake2);
        LWJGL_Controller2 newone = new LWJGL_Controller2(gb,mainwindow,shader,cam,mainboard);
        newone.run();
    }
}
