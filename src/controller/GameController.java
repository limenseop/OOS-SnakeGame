/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.controller;

import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import src.board.Board;
import src.board.TileRenderer;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.Ranking;
import src.domain.Renderer;
import src.font.FontRenderer;
import src.font.FontTexture;
import src.models.Camera;
import src.models.DualCamera;
import src.models.Shader;
import src.windowhandle.MouseHandler;
import src.windowhandle.Window;


import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

public class GameController {

    private Window mainwindow;
    private Shader shader;
    private Camera cam;
    private Board mainboard;
    private Renderer renderer;
    private MouseHandler mouseListener;
    private boolean on_Running = true;
    private String input= "";
    private TileRenderer tilerender;

    private GameState state = GameState.GAME_ACTIVE;
    private GameBoard gameboard;
    private Board dualboard;
    private DualCamera dualcam;

    public GameController(GameBoard gameboard, Window window, Shader shader, Camera cam, Board board){
        this.mainwindow = window;
        this.shader = shader;
        this.cam = cam;
        this.gameboard = gameboard;
        this.mainboard = board;
        tilerender = new TileRenderer();
        dualboard = new Board(board.getWidth()*2, board.getHeight()*2, board.getScale());
        dualcam = new DualCamera(mainwindow.getWidth(), mainwindow.getHeight(), dualboard.getWidth() * 2);
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
                            gameboard.change_Direction_Snake(Direction.EAST,0);
                        }
                        if (key == 263 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST,0);
                        }
                        if (key == 265 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH,0);
                        }
                        if (key == 264 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.SOUTH,0);
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
                    case GAME_DUAL -> {
                        if (key == 262 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.EAST,1);
                        }
                        if (key == 263 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST,1);
                        }
                        if (key == 265 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH,1);
                        }
                        if (key == 264 && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.SOUTH,1);
                        }
                        if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS ){
                            gameboard.change_Direction_Snake(Direction.SOUTH,0);
                        }
                        if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS ){
                            gameboard.change_Direction_Snake(Direction.NORTH,0);
                        }
                        if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS ){
                            gameboard.change_Direction_Snake(Direction.WEST,0);
                        }
                        if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS ){
                            gameboard.change_Direction_Snake(Direction.EAST,0);
                        }

                        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_MENU;
                        }

                        if (key == GLFW.GLFW_KEY_P && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_MENU;
                        }
                        break;
                    }
                   case GAME_TYPING -> {
                        if(action == GLFW.GLFW_PRESS  && isValid(key)){
                            input = input + (char)key;
                        }
                        else if(action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ENTER){
                            gameboard.setNickname(input);
                            input = "";
                            state = GameState.GAME_ACTIVE;
                        }
                        else if(action == GLFW.GLFW_PRESS && (key == GLFW.GLFW_KEY_DELETE || key==GLFW.GLFW_KEY_BACKSPACE)){
                            int length = input.length();
                            System.out.println("length = " + length);
                            if(length == 0) return;
                            input = input.substring(0,length-1);
                        }
                        break;
                    }
                    case GAME_RANKING -> {
                        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_INIT;
                        }
                    }
                }
            }
        };
        GLFW.glfwSetKeyCallback(mainwindow.getWindow(),keyCallback_ESC);
        renderer.setBoard(gameboard);
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
                }
                case GAME_DUAL -> {
                    //TODO dual모드 추가 사항 작성 요 -> ACTIVE와 딱히 다른게 필요한가? 필요없으면 DUAL을 굳이 나눌필요가 있나?
                }
                case GAME_ACTIVE -> {
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        /*
                        renderer.setBoard(gameboard);
                        mainboard.correctCameara(cam);
                        mainboard.render(tilerender, shader, cam);
                        renderer.render(shader,cam,mainboard);
                        renderer.scoreRender();
                        mainwindow.swapBuffer();
                        */
                        renderer.setBoard(gameboard);
                        mainboard.correctCameara(dualcam);
                        mainboard.render(tilerender, shader, dualcam);
                        renderer.renderforDual(shader,dualcam,mainboard);
                        renderer.scoreRender();
                        mainwindow.swapBuffer();
                    }
                    break;
                }
                case GAME_MENU -> {
                    mouseOptionHandle();
                    mainwindow.update();
                    mainboard.correctCameara(cam);
                    renderer.pausemenurender(shader,mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_TYPING -> {
                    mainwindow.update();
                    mainboard.correctCameara(cam);
                    mainboard.render(tilerender, shader, cam);
                    renderer.inputRendering(shader,mainboard);
                    renderer.nicknameRender(input);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
                case GAME_RANKING -> {
                    mainwindow.update();
                    mainboard.correctCameara(cam);
                    renderer.rankingRendering(shader,mainboard);
                    renderer.rankingRender();
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
            }
        }
        state = GameState.GAME_INIT;
        gameboard.re_Play(1);
        renderer.setZeroFocus(cam,mainboard);
        Dual_mode(dev)
        renderer.setBoard(gameboard);

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
                    gameboard.re_Play(1);
                    state = GameState.GAME_TYPING;
                    renderer.setBoard(gameboard);
                }
                else if(cursorX >= 253 && cursorX<=396 && cursorY>=311 && cursorY<=360){
                    try {
                        gameboard.loadGame();
                        renderer.setBoard(gameboard);
                        mainboard.correctCameara(cam);
                        mainboard.render(tilerender, shader, cam, mainwindow);
                    }catch(Exception e){
                        System.out.println("e = " + e);
                        gameboard.re_Play(1);
                    }
                    state = GameState.GAME_ACTIVE;
                }
                else if(cursorX>=191 && cursorX<=460 && cursorY>=398 && cursorY<=446){
                    state = GameState.GAME_RANKING;
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
                else if(false){
                    //TODO dualmode진입 조건 달기
                    gameboard.re_Play(2);
                    state = GameState.GAME_DUAL;
                    renderer.setBoard(gameboard);
                }
                break;
            }
            case GAME_MENU -> {
                if(cursorX>=206 && cursorX<=441 && cursorY >=272 && cursorY<=320){
                    state = GameState.GAME_ACTIVE;
                    //resume
                }
                else if(cursorX>=195 && cursorX<=445 && cursorY>=356 && cursorY<=405){
                    gameboard.re_Play(1);
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
