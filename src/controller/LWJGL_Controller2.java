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


public class LWJGL_Controller2 {

    private Window mainwindow;
    private Shader shader;
    private Camera cam;
    private Board mainboard;
    private Renderer renderer;
    private MouseHandler mouseListener;
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
        Recordingname.SimpleJButton button = recordingname.new SimpleJButton();
        init();
        loop();
        terminate();
    }

    private void init(){
        mouseListener = new MouseHandler(mainwindow.getWindow());
        state = GameState.GAME_ACTIVE;
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
                            state = GameState.GAME_PAUSED;
                        }
                        break;
                    }

                    /*case GAME_MENU -> {
                        if(key == GLFW.GLFW_KEY_1 && action == GLFW.GLFW_PRESS){
                            state = GameState.GAME_ACTIVE;
                            //resume
                        }
                        if(key == GLFW.GLFW_KEY_2 && action == GLFW.GLFW_PRESS){
                            gameboard.re_Play();
                            mainboard.correctCameara(cam, mainwindow);
                            mainboard.render(shader, cam);
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
                    }*/

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
                        renderer.setBoard(gameboard);
                        //

                        //mainboard.correctCameara(cam, mainwindow);
                        mainboard.render(shader, cam);
                        renderer.render(shader,cam,mainboard, gameboard.getSnakedirection());
                        mainwindow.swapBuffer();
                        System.out.println("fps:"+mainwindow.getcurrentFps());
                    }
                    break;
                }
                case GAME_PAUSED -> {
                    double mouse_X = mouseListener.getMousePressedX();
                    double mouse_Y = mouseListener.getMousePressedY();
                    mouseListener.eventsUpdater();
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    renderer.render(shader,cam,mainboard, gameboard.getSnakedirection());
                    renderer.mainmenurender(shader, mainboard);
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    System.out.println(recordingname.getName());
                    break;
                }
                case GAME_MENU -> {
                    mouseListener.eventsUpdater();
                    mainwindow.update();
                    mainboard.correctCameara(cam, mainwindow);
                    mainboard.render(shader, cam);
                    renderer.render(shader,cam,mainboard, gameboard.getSnakedirection());
                    mainwindow.swapBuffer();
                    mainwindow.timeHandle();
                    break;
                }
            }
        }
    }
}
