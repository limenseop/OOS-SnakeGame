package src.WindowHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.nio.DoubleBuffer;

public class Window implements EventLister {
    private int width, height;
    private double max_fps, time, processedtime = 0;
    private String windowtitle;
    private long window;
    private boolean keys[] = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean mousebuttons[] = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    public Window(int width, int height, int fps, String windowtitle) {
        this.width = width;
        this.height = height;
        this.max_fps = 1.0/fps;
        this.windowtitle = windowtitle;
    }
    private Window() {

    }
    public void createwindow() {
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
        GLFWVidMode videomode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (videomode.width() - width) / 2, (videomode.height() - height) / 2);
        GLFW.glfwShowWindow(window);

        time = getTime();
    }
    public boolean closewindow() {
        return GLFW.glfwWindowShouldClose(window);
    }
    public void update() {
        eventsUpdater();
        GLFW.glfwPollEvents();
    }
    public void swapBuffer() {
        GLFW.glfwSwapBuffers(window);
    }
    public boolean isKeyDown(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == 1;
    }
    public boolean isMouseDown(int mouseButton) {
        return GLFW.glfwGetMouseButton(window, mouseButton) == 1;
    }
    public boolean isKeyPressed(int keyCode) {
        return isKeyDown(keyCode) && !keys[keyCode];
    }
    public boolean isMousePressed(int mouseButton) {
        return isKeyDown(mouseButton) && !keys[mouseButton];
    }
    public boolean isKeyReleased(int keyCode) {
        return !isKeyDown(keyCode) && keys[keyCode];
    }
    public boolean isMouseReleased(int mouseButton) {
        return !isMouseDown(mouseButton) && mousebuttons[mouseButton];
    }
    public void eventsUpdater() {
        for (int i = 0; i < GLFW.GLFW_KEY_LAST; i++)
            keys[i] = isKeyDown(i);
        for (int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++)
            mousebuttons[i] = isMouseDown(i);
    }
    public double getMouseX() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, buffer, null);
        return buffer.get(0);
    }
    public double getMouseY() {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, null, buffer);
        return buffer.get(0);
    }

    public double getTime() {
        return (double)System.nanoTime() / (double)1000000000;
    }

    public boolean isUpdating() {
        double nexttime = getTime();
        double passedtime = nexttime - time;
        processedtime = passedtime;
        time = nexttime;

        while(processedtime > max_fps) {
            processedtime -= max_fps;
            return true;
        }
        return false;
    }
}
