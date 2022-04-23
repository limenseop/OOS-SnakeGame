package src;

import src.board.TileRenderer;
import src.windowhandle.Window;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";

    public static final float snake_speed = 0.1f;

    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        mainwindow.setBackgroundcolor(0.0f, 0.0f, 0.0f);
        Cute_snake snake = new Cute_snake(1, snake_speed, mainwindow.getWidth(), mainwindow.getHeight());
        TileRenderer tiles = new TileRenderer();
        while(!mainwindow.closewindow()){
            if (mainwindow.isUpdating()) {
                mainwindow.update();
                snake.move(mainwindow.getDirection());
                mainwindow.swapBuffer();
            }
        }
        mainwindow.stop();
    }
}
