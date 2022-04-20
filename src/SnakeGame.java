package src;

import src.WindowHandler.Window;
import org.lwjgl.glfw.GLFW;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        mainwindow.createwindow();

        while(!mainwindow.closewindow()){
            if(mainwindow.isUpdating()) {
                mainwindow.update();
                if (mainwindow.isKeyPressed(GLFW.GLFW_KEY_A))
                    System.out.println("HEY!!");
                if (mainwindow.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
                    System.out.println("Click position...: " + mainwindow.getMouseX() + ", " + mainwindow.getMouseY());
                mainwindow.swapBuffer();
            }
        }

    }
}
