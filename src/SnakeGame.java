package src;

import src.controller.LWJGL_Controller2_v2;
import src.domain.GameBoard;
import src.domain.Snake;
import src.windowhandle.Window;

public class SnakeGame {

    static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";


    public static void main(String[] args) {
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        Snake snake2 = new Snake();
        GameBoard gb = new GameBoard(snake2);
        LWJGL_Controller2_v2 newone = new LWJGL_Controller2_v2(gb,mainwindow);
        newone.run();
    }

}
