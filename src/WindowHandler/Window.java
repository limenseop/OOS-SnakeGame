package src.WindowHandler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import src.Model.Cute_snake;

public class Window {
    private int width, height, fps_cap;
    private double time, processedtime = 0;
    private String windowtitle;
    private long window;
    private float[] backgroundcolor = new float[] {0.0f, 0.0f, 0.0f};
    private Cute_snake snake;

    private Window() {

    }
    public Window(int width, int height, int fps, String windowtitle) {
        this.width = width;
        this.height = height;
        this.fps_cap = fps;
        this.windowtitle = windowtitle;
        createwindow();
    }
    public void setBackgroundcolor(float r, float g, float b) {
            backgroundcolor[0] = r;
            backgroundcolor[1] = g;
            backgroundcolor[2] = b;
    }
    public void stop() {
        GLFW.glfwTerminate();
    }
    public void loop() {
        while(!closewindow()){
            if (isUpdating()) {
                update();
                swapBuffer();
            }
        }
    }


    private void createwindow() {
        if (!GLFW.glfwInit()) {
            System.err.println("Error!!: GLFW.glfwinit() doesn't work.");
            return;
        }
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, windowtitle, 0, 0);

        if (window == 0) {
            System.err.println("Error!!: window doesn't created.");
            return;
        }
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        snake = new Cute_snake(1, window, width, height);

        GLFWVidMode videomode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (videomode.width() - width) / 2, (videomode.height() - height) / 2);
        GLFW.glfwShowWindow(window);

        time = getTime();
    }
    private void swapBuffer() {
        GLFW.glfwSwapBuffers(window);
    }
    private void update() {
        GLFW.glfwPollEvents();
        GL11.glClearColor(backgroundcolor[0], backgroundcolor[1], backgroundcolor[2], 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        snake.run();
    }
    private boolean closewindow() {
        return GLFW.glfwWindowShouldClose(window);
    }
    private double getTime() {
        return (double) System.nanoTime() / (double) 1000000000;
    }
    private boolean isUpdating() {
        double nexttime = getTime();
        double passedtime = nexttime - time;
        processedtime += passedtime;
        time = nexttime;
        while (processedtime > (1.0 / fps_cap)) {
            processedtime -= 1.0 / fps_cap;
            return true;
        }
        return false;
    }
}
