package src.controller;

import org.lwjgl.glfw.*;
import src.board.Board;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.Renderer;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;
import src.windowhandle.MouseHandler;
import src.windowhandle.Window;


import java.awt.geom.Point2D;
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
    private Renderer renderer;
    private Texture snakeTex;
    private Texture appleTex;
    private MouseHandler mouseListener;


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
        mouseListener = new MouseHandler(mainwindow.getWindow());
        state = GameState.GAME_ACTIVE;
        renderer = new Renderer();
        snakeTex = new Texture("Cute-Snake-Transparent-PNG.png");
        appleTex = new Texture("tile2.png");
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
                case GAME_INIT -> {

                }
                case GAME_ACTIVE -> {
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();

                        //
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        renderer.setBoard(gameboard);
                        //

                        mainboard.correctCameara(cam, mainwindow);

                        mainboard.render(shader, cam);
                        renderer.render(shader,cam,mainboard);
                        mainwindow.swapBuffer();
                        //System.out.println("fps:"+mainwindow.getcurrentFps());
                    }
                    break;
                }
                case GAME_PAUSED -> {
                    double mouse_X = mouseListener.getMousePressedX();
                    double mouse_Y = mouseListener.getMousePressedY();
                    mouseListener.eventsUpdater();
                    mouseHandle(mouse_X,mouse_Y);
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    renderer.render(shader,cam,mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_MENU -> {
                    double mouse_X = mouseListener.getMousePressedX();
                    double mouse_Y = mouseListener.getMousePressedY();
                    mouseListener.eventsUpdater();
                    mouseHandle(mouse_X,mouse_Y);
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    renderer.render(shader,cam,mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_TERMINATE -> {

                }
            }
        }
    }

    private void mouseHandle(double cursorX,double cursorY){
        if(cursorX == 0 && cursorY == 0) return;
        Point2D cursor = new Point2D.Float((float) cursorX, (float) cursorY);
        System.out.println("cursor = " + cursor);
        switch (state){
            case GAME_MENU -> {
                System.out.println("hello_menu");
                /*if(){
                    state = GameState.GAME_ACTIVE;
                    //resume
                }
                else if(){
                    gameboard.re_Play();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    state = GameState.GAME_ACTIVE;
                    mainwindow.timeHandle();
                    //replay
                }
                else if(){
                    try {
                        gameboard.save_This_Game();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //save
                }
                else if(){
                    gameboard.loadGame();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    //load game
                }*/
                break;
            }
            case GAME_PAUSED -> {
                System.out.println("hello pause");
                /*if(){
                    state = GameState.GAME_ACTIVE;
                    //resume
                }*/
                break;
            }
        }
    }

    private void game_Terminate(){

    }


}
