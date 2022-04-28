package src.controller;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;
import src.board.Board;
import src.domain.GameBoard;
import src.domain.Direction;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class LWJGL_Controller2 {

    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static final int BOARD_WIDTH = 42;
    public static final int BOARD_HEIGHT = 42;
    public static final int BOARD_SCALE = 16;
    public static final float SNAKE_SPEED = 0.3f;

    private Window mainwindow;
    private Shader shader;
    private Camera cam;
    private Board mainboard;


    private GameState state = GameState.GAME_ACTIVE;
    private GameBoard gameboard;

    public LWJGL_Controller2(GameBoard gameboard,Window window, Shader shader,Camera cam,Board board){
        this.mainwindow = window;
        this.shader = shader;
        this.cam = cam;
        this.gameboard = gameboard;
        this.mainboard = board;
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
                            gameboard.update(cam,mainboard);
                            mainboard.correctCameara(cam, mainwindow);

                            mainboard.render(shader, cam);
                            gameboard.render(shader,cam);
                            state = GameState.GAME_ACTIVE;
                            mainwindow.timeHandle();
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
                            gameboard.update(cam,mainboard);
                            mainboard.correctCameara(cam, mainwindow);

                            mainboard.render(shader, cam);
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
                        gameboard.check_Fruit_Overlap(mainwindow);
                        gameboard.check_Game_Terminated();
                        gameboard.update(cam,mainboard);
                        //

                        mainboard.correctCameara(cam, mainwindow);

                        mainboard.render(shader, cam);
                        gameboard.render(shader,cam);
                        mainwindow.swapBuffer();
                        System.out.println("fps:"+mainwindow.getcurrentFps());
                    }
                    break;
                }
                case GAME_PAUSED -> {
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    gameboard.render(shader,cam);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_MENU -> {
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    gameboard.render(shader,cam);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
            }
        }
    }
}
