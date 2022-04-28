package src.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;
import src.collision.AABB;
import src.models.Basicmodel;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;

public class Entity_beta {
    private Basicmodel model;
    private Texture tex;
    private Transform transform;
    private AABB bounding_box;

    public Entity_beta(float x, float y) {
        model = new Basicmodel();
        tex= new Texture("pngwing.com.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(x, y, 0);
        bounding_box = new AABB(
                new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1,1), false);
    }
    public void update(Entity snake) {
        transform.pos = new Vector3f(snake.getTransform().pos.x+3, snake.getTransform().pos.y+3, 0);
        bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
    }
    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        tex.bind(0);
        model.render();
    }
    public Transform getTransform() {
        return transform;
    }
    public AABB getBounding_box() {
        return bounding_box;
    }
}
