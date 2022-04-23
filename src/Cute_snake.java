package src;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.Direction;
import src.models.Basicmodel;
import src.models.Shader;
import src.models.Texture;

public class Cute_snake extends Basicmodel {
    private Texture tex;
    private Shader shader;
    private Matrix4f projection;
    private Matrix4f scale;
    private Matrix4f target;

    private float x, y;
    private float speed;

    public Cute_snake(float size, float speed, int window_width, int window_height) {
        super(size);
        this.speed = speed;
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
    private void direct(Direction direction) {
        if (direction == Direction.WEST) {
            x = -speed;
            y = 0.0f;
            System.out.println("LEFT");
        }
        if (direction == Direction.EAST) {
            x = speed;
            y = 0.0f;
            System.out.println("RIGHT");
        }
        if (direction == Direction.NORTH) {
            x = 0.0f;
            y = speed;
            System.out.println("UP");
        }
        if (direction == Direction.SOUTH) {
            x = 0.0f;
            y = -speed;
            System.out.println("DOWN");
        }
        scale.translate(new Vector3f(x, y, 0));
        projection.mul(scale,target);
    }
    public void move(Direction direction) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        tex.bind(0);
        direct(direction);
        super.render();
    }
}
