/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src;

import src.board.Board;
import src.domain.EntitySnake;
import src.entity.Entity;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static final int BOARD_WIDTH = 42;
    public static final int BOARD_HEIGHT = 42;
    public static final int BOARD_SCALE = 16;
    public static final float SNAKE_SPEED = 0.3f;

    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        Shader shader = new Shader("shader");
        Camera cam = new Camera(mainwindow.getWidth(), mainwindow.getHeight());
        Board mainboard = new Board(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SCALE);
        Entity snake = new Entity(42, -43, SNAKE_SPEED);

        while(!mainwindow.close()){
            if (mainwindow.isUpdating()) {
                mainwindow.update();
                snake.update(mainwindow, cam, mainboard);
                mainboard.correctCameara(cam, mainwindow);

                mainboard.render(shader, cam);
                snake.render(shader,cam);
                mainwindow.swapBuffer();
                System.out.println("fps:"+mainwindow.getcurrentFps());
            }
        }
        mainwindow.stop();
    }
}
