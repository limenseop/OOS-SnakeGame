package src.Model;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Cute_snake extends Model_with_texture {
    private EventListener listener;
    private Texture tex;
    private Shader shader;
    private Matrix4f projection;
    private Matrix4f scale;
    private Matrix4f target;

    private float x, y;
    public Cute_snake(int size, long window, int window_width, int window_height) {
        super(size);
        listener = new EventListener(window);
        shader = new Shader("shader");
        tex = new Texture("imgfolder/Cute-Snake-Transparent-PNG.png");
        projection = new Matrix4f()
                .ortho2D(-window_width/2, window_width/2, -window_height/2, window_height/2);
        scale = new Matrix4f()
                .translate(new Vector3f(0,0,0))
                .scale(32);
        target = new Matrix4f();

        projection.mul(scale,target);
    }
    private void movements() {
        if (listener.isKeyPressed(GLFW_KEY_LEFT) && !listener.isKeyReleased(GLFW_KEY_LEFT)) {
            x = -0.1f;
            y = 0.0f;
            System.out.println("LEFT");
        }
        if (listener.isKeyPressed(GLFW_KEY_RIGHT) && !listener.isKeyReleased(GLFW_KEY_RIGHT)) {
            x = 0.1f;
            y = 0.0f;
            System.out.println("RIGHT");
        }
        if (listener.isKeyPressed(GLFW_KEY_UP) && !listener.isKeyReleased(GLFW_KEY_UP)) {
            x = 0.0f;
            y = 0.1f;
            System.out.println("UP");
        }
        if (listener.isKeyPressed(GLFW_KEY_DOWN) && !listener.isKeyReleased(GLFW_KEY_DOWN)) {
            x = 0.0f;
            y = -0.1f;
            System.out.println("DOWN");
        }
    }
    private void move() {
        movements();
        scale.translate(new Vector3f(x, y, 0));
        projection.mul(scale,target);
    }
    public void run() {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        tex.bind(0);
        move();
        listener.eventsUpdater();
        super.render();
    }
}
