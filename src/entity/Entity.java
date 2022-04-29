package src.entity;

import org.joml.Vector3f;
import src.models.*;

import java.awt.*;

public class Entity {
    protected Snakemodel model;
    protected Texture tex;
    protected Transform transform;

    public Entity(float x, float y, String filename) {
        model = new Snakemodel();
        tex = new Texture(filename);
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(x, y, 0);
    }
    public void update(Point position) {
        transform.pos.set((float)position.x * 2, (float)position.y * -2, 0);
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        tex.bind(0);
        model.render();
    }

}
