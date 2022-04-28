/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src;

import src.board.Board;
import src.entity.Entity;
import src.entity.Entity_beta;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static final int BOARD_WIDTH = 40;
    public static final int BOARD_HEIGHT = 40;
    public static final int BOARD_SCALE = 16;
    public static final float SNAKE_SPEED = 0.3f;

    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        Shader shader = new Shader("shader");
        Camera cam = new Camera(mainwindow.getWidth(), mainwindow.getHeight());
        Board mainboard = new Board(BOARD_WIDTH+2, BOARD_HEIGHT+2, BOARD_SCALE);
        Entity snake = new Entity((float) BOARD_WIDTH, -(float) BOARD_HEIGHT, SNAKE_SPEED);
        Entity_beta snakebody = new Entity_beta((float) BOARD_WIDTH + 10, -(float) BOARD_HEIGHT -10);

        while(!mainwindow.close()){
            if (mainwindow.isUpdating()) {
                mainwindow.update();
                snake.update(mainwindow, cam, mainboard, snakebody);
                snakebody.update(snake);
                mainboard.correctCameara(cam, mainwindow);
                mainboard.appleupdate(snake);

                mainboard.render(shader, cam);
                snake.render(shader, cam);
                snakebody.render(shader, cam);
                mainwindow.swapBuffer();
            }
        }
        mainwindow.stop();
    }
}
