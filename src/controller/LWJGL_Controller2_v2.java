package src.controller;

import org.lwjgl.glfw.*;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.GameState;
import src.entity.RenderSnake;
import src.windowhandle.Window;


import java.io.IOException;

import static java.lang.Thread.sleep;

public class LWJGL_Controller2_v2 {

    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int BOARD_WIDTH = 40;
    public static final int BOARD_HEIGHT = 40;

    private Window mainwindow;

    private RenderSnake rendersnake;
    private GameState state = GameState.GAME_ACTIVE;
    private GameBoard gameboard;

    public LWJGL_Controller2_v2(GameBoard gameboard, Window window){
        this.mainwindow = window;
        this.gameboard = gameboard;
        rendersnake = new RenderSnake(BOARD_WIDTH, BOARD_HEIGHT, MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT);
    }


    public void run(){
        init();
        loop();
        terminate();
    }

    private void init(){
        state = GameState.GAME_ACTIVE;
        GLFWKeyCallback keyCallback_ESC = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                switch (state) {

                    case GAME_ACTIVE -> {
                        //snake 방향변경
                        if (key == 262 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.EAST);
                        }
                        if (key == 263 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST);
                        }
                        if (key == 265 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH);
                        }
                        if (key == 264 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.SOUTH);
                        }

                        //메뉴로 이동
                        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_MENU;
                        }

                        if (key == GLFW.GLFW_KEY_P && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_PAUSED;
                        }
                        break;
                    }

                    case GAME_MENU -> {
                        if(key == GLFW.GLFW_KEY_1 && action == GLFW.GLFW_PRESS){
                            state = GameState.GAME_ACTIVE;
                            //resume
                        }
                        if(key == GLFW.GLFW_KEY_2 && action == GLFW.GLFW_PRESS){
                            gameboard.re_Play();
                            gameboard.update();

                            state = GameState.GAME_ACTIVE;
                            //restart game
                        }
                        if(key==GLFW.GLFW_KEY_3 && action == GLFW.GLFW_PRESS){
                            try {
                                gameboard.save_This_Game();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //save game
                        }
                        if(key==GLFW.GLFW_KEY_4 && action == GLFW.GLFW_PRESS){
                            gameboard.loadGame();
                            gameboard.update();

                            //gameboard.render(shader,cam);
                            state = GameState.GAME_ACTIVE;
                            //load game
                        }
                        if(key==GLFW.GLFW_KEY_5 && action == GLFW.GLFW_PRESS){
                            gameboard.gameTerminate();
                            //game terminate
                        }
                    }

                    case GAME_PAUSED -> {
                        if (key == GLFW.GLFW_KEY_P && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_ACTIVE;
                        }
                    }

                }
            }
        };
        GLFW.glfwSetKeyCallback(mainwindow.getWindow(),keyCallback_ESC);
    }

    private void terminate(){
        Callbacks.glfwFreeCallbacks(mainwindow.getWindow());
        GLFW.glfwDestroyWindow(mainwindow.getWindow());
        //GLFW.glfwSetErrorCallback(null).free();
    }

    private void loop() {
        while(gameboard.gameRunning()){
            switch(state){
                case GAME_ACTIVE -> {
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();
                        //
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        rendersnake.update(gameboard.getHeadDirection(), gameboard.getSnakePoisition(), gameboard.getFruitPosition(), mainwindow);
                        gameboard.update();
                        gameboard.brief();
                        rendersnake.renderall();
                        //
                        try {
                            sleep(110);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mainwindow.swapBuffer();
                        System.out.println("fps:"+mainwindow.getcurrentFps());
                    }
                    break;
                }
                case GAME_PAUSED -> {
                    mainwindow.update();
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_MENU -> {
                    mainwindow.update();
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
            }
        }
    }
}
