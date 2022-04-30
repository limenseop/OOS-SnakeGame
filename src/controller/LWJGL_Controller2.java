package src.controller;

import org.lwjgl.glfw.*;
import src.board.Board;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.Renderer;
import src.models.Camera;
import src.models.Shader;
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
    private MouseHandler mouseListener;
    private boolean on_Running = true;
    private String input= "";
    private Recordingname recordingname;


    private GameState state = GameState.GAME_ACTIVE;
    private GameBoard gameboard;

    public LWJGL_Controller2(GameBoard gameboard,Window window, Shader shader,Camera cam,Board board){
        this.mainwindow = window;
        this.shader = shader;
        this.cam = cam;
        this.gameboard = gameboard;
        this.mainboard = board;
        recordingname = new Recordingname();
    }


    public void run(){
        init();
        loop();
        terminate();
    }

    private void init(){
        mouseListener = new MouseHandler(mainwindow.getWindow());
        state = GameState.GAME_INIT;
        renderer = new Renderer();
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
                            state = GameState.GAME_MENU;
                        }
                        break;
                    }
                   /*case GAME_TYPING -> {
                        if(action == GLFW.GLFW_PRESS  && isValid(key)){
                            input = input + (char)key;
                            System.out.println("input = " + input);
                        }
                        else if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ENTER){
                            gameboard.setNickname(input);
                            input = "";
                        }
                    }*/
                }
            }
        };
        GLFW.glfwSetKeyCallback(mainwindow.getWindow(),keyCallback_ESC);
        }

    private void terminate(){
        Callbacks.glfwFreeCallbacks(mainwindow.getWindow());
        GLFW.glfwDestroyWindow(mainwindow.getWindow());
    }

    private void loop() {
    while(on_Running){
        while(gameboard.gameRunning()){
            switch(state){
                case GAME_INIT -> {
                    if(mainwindow.isUpdating()) {
                        mouseOptionHandle();
                        mainwindow.update();
                        renderer.mainmenurender(shader, mainboard);
                        mainwindow.swapBuffer();
                    }
                    break;
                }
                case GAME_ACTIVE -> {
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        renderer.setBoard(gameboard);
                        mainboard.correctCameara(cam, mainwindow);
                        mainboard.render(shader, cam);
                        renderer.render(shader,cam,mainboard);
                        mainwindow.swapBuffer();
                    }
                    break;
                }
                case GAME_MENU -> {
                    mouseOptionHandle();
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    renderer.pausemenurender(shader,mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                /*case GAME_TYPING -> {
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    //renderer.render(shader,cam,mainboard);
                    //renderer.pausemenurender(shader,mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }*/
            }
        }
        state = GameState.GAME_INIT;
        gameboard.re_Play();
        renderer.setZeroFocus(cam,mainboard);
    }
    }

    private void mouseOptionHandle() {
        double mouse_X = mouseListener.getMousePressedX();
        double mouse_Y = mouseListener.getMousePressedY();
        mouseListener.eventsUpdater();
        mouseHandle(mouse_X,mouse_Y);
    }

    private void mouseHandle(double cursorX,double cursorY){
        if(cursorX == 0 && cursorY == 0) return;
        Point2D cursor = new Point2D.Float((float) cursorX, (float) cursorY);
        System.out.println("cursor = " + cursor);
        switch (state){
            case GAME_INIT -> {
                if(cursorX>=253 && cursorX<=396 && cursorY>=224 && cursorY<=270){
                    gameboard.re_Play();
                    state = GameState.GAME_ACTIVE;
                }
                else if(cursorX >= 253 && cursorX<=396 && cursorY>=311 && cursorY<=360){
                    try {
                        gameboard.loadGame();
                        mainboard.correctCameara(cam, mainwindow);
                        mainboard.render(shader, cam);
                    }catch(Exception e){
                        System.out.println("e = " + e);
                    }
                    state = GameState.GAME_ACTIVE;
                }
                else if(cursorX>=191 && cursorX<=460 && cursorY>=398 && cursorY<=446){
                    gameboard.showRanking();
                }
                else if(cursorX>=262 && cursorX<=390 && cursorY>=490 && cursorY<=535){
                    try {
                        gameboard.save_Ranking();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gameboard.gameTerminate();
                    on_Running = false;
                }
            }
            case GAME_MENU -> {
                if(cursorX>=206 && cursorX<=441 && cursorY >=272 && cursorY<=320){
                    state = GameState.GAME_ACTIVE;
                    //resume
                }
                else if(cursorX>=195 && cursorX<=445 && cursorY>=356 && cursorY<=405){
                    gameboard.re_Play();
                    state = GameState.GAME_ACTIVE;
                    //replay
                }
                else if(cursorX>=252 && cursorX<=395 && cursorY>=443 && cursorY<=492){
                    try {
                        gameboard.save_This_Game();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //game save
                }
                else if(cursorX>=263 && cursorX<=385 && cursorY>=530 && cursorY<=574){
                    state = GameState.GAME_INIT;
                }
                break;
            }
        }
    }

    private boolean isValid(int ascii){
        if((ascii>=48 && ascii<=57)
        ||(ascii>=65 && ascii<=90)
        ||(ascii>=97 && ascii<=122)) return true;
        return false;
    }
}
