package src.WindowHandler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import src.Model.Model_with_texture;
import src.Model.Texture;
import src.maths.Vector3f;

public class Window {
    private int width, height, fps_cap;
    private double time, processedtime = 0;
    private String windowtitle;
    private EventListener listener;
    private long window;
    private Vector3f backgroundcolor = new Vector3f(0.0f, 0.0f, 0.0f);
    private Model_with_texture model;
    private Texture tex;
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
        backgroundcolor.setAll(r, g, b);
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

        model = new Model_with_texture(0.5f);
        tex = new Texture("imgfolder/Cute-Snake-Transparent-PNG.png");

        GLFWVidMode videomode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (videomode.width() - width) / 2, (videomode.height() - height) / 2);
        GLFW.glfwShowWindow(window);

        time = getTime();
        listener = new EventListener(window);
    }
    private void swapBuffer() {
        GLFW.glfwSwapBuffers(window);
    }
    private void update() {
        GLFW.glfwPollEvents();
        listener.eventsUpdater();
        GL11.glClearColor(backgroundcolor.getX(), backgroundcolor.getY(), backgroundcolor.getZ(), 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        tex.bind();
        model.render();

/*
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(-0.5f, 0.5f);

        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(0.5f, 0.5f);

        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(0.5f, -0.5f);

        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(-0.5f, -0.5f);

        GL11.glEnd();
*/

        if (listener.isKeyPressed(GLFW.GLFW_KEY_UP))
            System.out.println("You just pressed...UP");
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
    public int getFps() {
        return fps_cap;
    }

    public String getWindowtitle() {
        return windowtitle;
    }

    public long getWindow() {
        return window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
