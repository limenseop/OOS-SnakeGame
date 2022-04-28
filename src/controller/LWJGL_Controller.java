package src.controller;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;
import src.domain.GameBoard;
import src.domain.Direction;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class LWJGL_Controller {


    private GameState state;
    private Thread controller;
    private GameBoard gameboard;
    private long window;
    private static int WIDTH = 80;
    private static int HEIGHT = 60;


    public void run(MainController controller){
        init(controller);
        loop();
        terminate();
    }

    private void init(MainController controller){
        state = GameState.GAME_ACTIVE;
        this.controller = new Thread(controller);
        this.gameboard = controller.getGameboard();
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,GLFW.GLFW_TRUE);
        window = GLFW.glfwCreateWindow(WIDTH,HEIGHT,"BreakOut", MemoryUtil.NULL,MemoryUtil.NULL);
        if(window==MemoryUtil.NULL){
            throw new RuntimeException("Failed to create the GLFW Window");
        }
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window,(vidMode.width()-WIDTH)/2,(vidMode.height()-HEIGHT)/2);

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
        GLFW.glfwSetKeyCallback(window,keyCallback_ESC);
        GLFW.glfwMakeContextCurrent(window);

        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);
        }

    private void terminate(){
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void loop(){
        while((gameboard.gameRunning())) {
            GLFW.glfwPollEvents();
            switch (state){
                case GAME_ACTIVE -> {
                    gameboard.move_Snake();
                    gameboard.check_Fruit_Overlap();
                    gameboard.check_Game_Terminated();

                    try {
                        sleep(300);
                     }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case GAME_PAUSED -> {
                    GLFW.glfwPollEvents();
                    break;
                }
                case GAME_MENU -> {
                    GLFW.glfwPollEvents();
                    break;
                }
            }
        }
    }


}
