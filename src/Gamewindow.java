package src;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Gamewindow {
    private long window;
    private final int windowWidthSize = 1000;
    private final int windowHeightSize = 1000;
    public void run() {
        //게임창 생성
        init();
        //게임창 유지
        loop();

        //메모리 복구
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        //게임창 종료
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Failed to create the window");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);


        window = glfwCreateWindow(windowWidthSize, windowHeightSize, "Snake Game", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - windowWidthSize) / 2,
                    (vidmode.height() - windowHeightSize) / 2
            );
        }

        glfwSetKeyCallback(window, KeyListener::keyCallback);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {

        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //게임창 실행중 설정
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);

            if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                System.out.println("You just pressed 상");
            }
            else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                System.out.println("You just pressed 하");
            }
            else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
                System.out.println("You just pressed 좌");
            }
            else if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
                System.out.println("You just pressed 우");
            }
            glfwPollEvents();
        }
    }
}