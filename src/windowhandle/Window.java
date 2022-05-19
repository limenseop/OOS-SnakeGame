/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.windowhandle;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private int width, height, fps_cap;
    private double time, processedtime = 0;
    private String windowtitle;
    private long window;
    private float[] backgroundcolor = new float[] {0.0f, 0.0f, 0.0f};
    private MouseHandler listener;

    public Window(int width, int height, int fps, String windowtitle) {
        this.width = width;
        this.height = height;
        this.fps_cap = fps;
        this.windowtitle = windowtitle;
        create();
    }
    public void stop() {
        GLFW.glfwTerminate();
    }
    private void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("Error!!: GLFW.glfwinit() doesn't work.");
            return;
        }
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        window = GLFW.glfwCreateWindow(width, height, windowtitle, 0, 0);

        if (window == 0) {
            System.err.println("Error!!: window doesn't created.");
            return;
        }
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        listener = new MouseHandler(window);

        GLFWVidMode videomode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (videomode.width() - width) / 2, (videomode.height() - height) / 2);
        GLFW.glfwShowWindow(window);
        GLFW.glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        time = getTime();
    }
    public void swapBuffer() {
        GLFW.glfwSwapBuffers(window);
    }
    public void update() {
        GLFW.glfwPollEvents();
        GL11.glClearColor(backgroundcolor[0], backgroundcolor[1], backgroundcolor[2], 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public static double getTime() {
        return GLFW.glfwGetTime();
    }

    public boolean isUpdating() {
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

    public void timeHandle(){
        time = getTime();
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public long getWindow() {
        return window;
    }

}
