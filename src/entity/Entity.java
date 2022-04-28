package src.entity;

import org.joml.Vector3f;
import src.board.Board;
import src.models.Basicmodel;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;
import java.awt.*;

public class Entity {
    protected Basicmodel model;
    protected Texture tex;
    protected Transform transform;

    public Entity(float x, float y, String filename) {
        model = new Basicmodel();
        tex = new Texture(filename);
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(x, y, 0);
    }
    public void update(Point position, Camera camera, Board board) {
        transform.pos.set((float)position.x * 2, (float)position.y * -2, 0);
        camera.getPosition().lerp(transform.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        tex.bind(0);
        model.render();
    }

}
