package src.WindowHandler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private int width, height, fps_cap;
    private double time, processedtime = 0;
    private String windowtitle;
    private EventListener listener;
    private long window;
    private float backgroundcolor[] = new float[3];
    private Window() {

    }
    public Window(int width, int height, int fps, String windowtitle) {
        this.width = width;
        this.height = height;
        this.fps_cap = fps;
        this.windowtitle = windowtitle;
        for (int i = 0; i < 3; i++)
            backgroundcolor[0] = 0.0f;
        createwindow();
    }
    public int getFps() {
        return fps_cap;
    }
    public String getWindowtitle() {
        return windowtitle;
    }
    public long getWindow() {
        return window;
    }
    public void setWindow(long window) {
        this.window = window;
    }
    public void setWindowtitle(String windowtitle) {
        this.windowtitle = windowtitle;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setFps(int fps_cap) {
        this.fps_cap = fps_cap;
    }
    public void setBackgroundcolor(float r, float g, float b) {
        backgroundcolor[0] = r;
        backgroundcolor[1] = g;
        backgroundcolor[2] = b;
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

        GLFWVidMode videomode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (videomode.width() - width) / 2, (videomode.height() - height) / 2);
        GLFW.glfwShowWindow(window);

        time = getTime();
        listener = new EventListener(window);
    }
    public boolean closewindow() {
        return GLFW.glfwWindowShouldClose(window);
    }
    public void stop() {
        GLFW.glfwTerminate();
    }
    public void update() {
        listener.eventsUpdater();
        GL11.glClearColor(backgroundcolor[0], backgroundcolor[1], backgroundcolor[2], 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GLFW.glfwPollEvents();
        if (listener.isKeyPressed(GLFW.GLFW_KEY_UP))
            System.out.println("You just pressed...UP");
    }
    public void swapBuffer() {
        GLFW.glfwSwapBuffers(window);
    }
    public double getTime() {
        return (double)System.nanoTime() / (double)1000000000;
    }
    public boolean isUpdating() {
        double nexttime = getTime();
        double passedtime = nexttime - time;
        processedtime += passedtime;
        time = nexttime;
        while(processedtime > (1.0/fps_cap)) {
            processedtime -= 1.0/fps_cap;
            return true;
        }
        return false;
    }

    public void loop() {
        while(!closewindow()){
            if (isUpdating()) {
                update();
                swapBuffer();
            }
        }
    }
}
