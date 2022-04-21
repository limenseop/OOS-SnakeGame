package src;

import src.Model.Model;
import src.Model.Renderer;
import src.WindowHandler.Window;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";

    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        mainwindow.setBackgroundcolor(1.0f, 0.0f, 0.0f);
        mainwindow.loop();
        mainwindow.stop();
    }
}
