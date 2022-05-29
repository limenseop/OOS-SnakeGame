/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.controller;

import org.lwjgl.glfw.*;
import src.board.Board;
import src.board.TileRenderer;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.Renderer;
import src.models.Camera;
import src.models.DualCamera;
import src.models.Shader;
import src.windowhandle.MouseHandler;
import src.windowhandle.Window;


import java.awt.geom.Point2D;
import java.io.IOException;

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
        dualcam = new DualCamera(mainwindow.getWidth(), mainwindow.getHeight(), dualboard.getWidth()/2);
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
                if(gameboard.isRunning() == false){
                    if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS){
                        System.out.println("hello");
                        state = GameState.GAME_INIT;
                        gameboard.re_Play(1);
                        renderer.setZeroFocus(cam,mainboard);
                        renderer.setBoard(gameboard);
                    }
                }
                switch (state) {
                    case GAME_ACTIVE -> {
                        //snake 방향변경
                        if (key == GLFW.GLFW_KEY_RIGHT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.EAST,0);
                        }
                        if (key == GLFW.GLFW_KEY_LEFT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST,0);
                        }
                        if (key == GLFW.GLFW_KEY_UP && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH,0);
                        }
                        if (key == GLFW.GLFW_KEY_DOWN && action == GLFW.GLFW_PRESS) {
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
                        if (key == GLFW.GLFW_KEY_RIGHT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.EAST,1);
                        }
                        if (key == GLFW.GLFW_KEY_LEFT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST,1);
                        }
                        if (key == GLFW.GLFW_KEY_UP && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH,1);
                        }
                        if (key == GLFW.GLFW_KEY_DOWN && action == GLFW.GLFW_PRESS) {
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
                    case GAME_AUTO -> {
                        if (key == GLFW.GLFW_KEY_RIGHT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.EAST,1);
                        }
                        if (key == GLFW.GLFW_KEY_LEFT && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.WEST,1);
                        }
                        if (key == GLFW.GLFW_KEY_UP && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.NORTH,1);
                        }
                        if (key == GLFW.GLFW_KEY_DOWN && action == GLFW.GLFW_PRESS) {
                            gameboard.change_Direction_Snake(Direction.SOUTH,1);
                        }
                        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_MENU;
                        }

                        if (key == GLFW.GLFW_KEY_P && action == GLFW.GLFW_PRESS) {
                            state = GameState.GAME_MENU;
                        }
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
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        dualboard.correctCameara(dualcam);
                        dualboard.render(tilerender, shader, dualcam);
                        renderer.renderforDual(shader,dualcam,mainboard);
                        renderer.scoreRender();
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
                        mainboard.correctCameara(cam);
                        mainboard.render(tilerender, shader, cam);
                        renderer.render(shader,cam,mainboard);
                        renderer.scoreRender();
                        mainwindow.swapBuffer();
                    }
                    break;
                }
                case GAME_AUTO -> {
                    if (mainwindow.isUpdating()) {
                        mainwindow.update();
                        gameboard.moveAutoSnake();
                        gameboard.move_Snake();
                        gameboard.check_Fruit_Overlap();
                        gameboard.check_Game_Terminated();
                        dualboard.correctCameara(dualcam);
                        dualboard.render(tilerender, shader, dualcam);
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
        if(on_Running) {
            mainwindow.update();
            mainboard.correctCameara(cam);
            renderer.rankingRendering(shader, mainboard);
            renderer.render_result();
            mainwindow.swapBuffer();
            mainwindow.timeHandle();
        }
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
                if(cursorX>=132 && cursorX<=522 && cursorY>=195 && cursorY<=245){
                    gameboard.re_Play(1);
                    state = GameState.GAME_TYPING;
                    renderer.setBoard(gameboard);
                }
                //dualmode
                else if(cursorX>=167 && cursorX<=487 && cursorY>=267 && cursorY<=317){
                    gameboard.re_Play(2);
                    state = GameState.GAME_DUAL;
                    renderer.setBoard(gameboard);
                }

                //automode
                else if(cursorX>=167 && cursorX<=487 && cursorY>=339 && cursorY<=389) {
                    gameboard.re_Play(2);
                    gameboard.set_Auto_Mode();
                    state = GameState.GAME_AUTO;
                    renderer.setBoard(gameboard);
                }

                else if(cursorX >= 252 && cursorX<=402 && cursorY>=411 && cursorY<=461){
                    try {
                        gameboard.loadGame();
                        mainboard.correctCameara(cam);
                        mainboard.render(tilerender, shader, cam);
                    }catch(Exception e){
                        System.out.println("e = " + e);
                        gameboard.re_Play(1);
                    }
                    renderer.setBoard(gameboard);
                    state = GameState.GAME_ACTIVE;
                }

                else if(cursorX>=192 && cursorX<=462 && cursorY>=483 && cursorY<=553){
                    state = GameState.GAME_RANKING;
                }
                else if(cursorX>=262 && cursorX<=392 && cursorY>=555 && cursorY<=605){
                    try {
                        gameboard.save_Ranking();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gameboard.gameTerminate();
                    on_Running = false;
                }
                break;
            }
            case GAME_MENU -> {
                if(cursorX>=206 && cursorX<=441 && cursorY >=272 && cursorY<=320){
                    if(gameboard.isAutoDual()){
                        if(gameboard.isAutoDual()){
                            if(gameboard.isAuto()) state = GameState.GAME_AUTO;
                            else state = GameState.GAME_DUAL;
                        }
                    }
                    else
                    state = GameState.GAME_ACTIVE;
                    //resume
                }
                else if(cursorX>=195 && cursorX<=445 && cursorY>=356 && cursorY<=405){
                    boolean checker = false;
                    System.out.println("replay option");
                    System.out.println("gameboard = " + gameboard.isAutoDual());
                    if(gameboard.isAutoDual()){
                        if(gameboard.isAuto()) checker = true;
                        gameboard.re_Play(2);
                        state = GameState.GAME_DUAL;
                        if(checker){
                            gameboard.set_Auto_Mode();
                            state = GameState.GAME_AUTO;
                        }
                    }
                    else{
                        gameboard.re_Play(1);
                        state = GameState.GAME_ACTIVE;
                    }
                    renderer.setBoard(gameboard);
                }
                else if(cursorX>=252 && cursorX<=395 && cursorY>=443 && cursorY<=492) {
                    if (gameboard.isAutoDual()) {
                        state = GameState.GAME_INIT;
                    }
                    else {
                        try {
                            System.out.println("saved");
                            gameboard.save_This_Game();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //game save
                    }
                }
                else if(!gameboard.isAutoDual()&&cursorX>=263 && cursorX<=385 && cursorY>=530 && cursorY<=574){
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
