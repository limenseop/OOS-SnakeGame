package src.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;
import src.Direction;
import src.board.Board;
import src.collision.AABB;
import src.collision.Collision;
import src.models.Basicmodel;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;
import src.windowhandle.Window;

public class Player {
    private Basicmodel model;
    private Texture tex;
    private Transform transform;
    private Vector3f direction;
    private AABB bounding_box;
    public Player(float x, float y) {
        model = new Basicmodel();
        tex= new Texture("Cute-Snake-Transparent-PNG.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(x, y, 0);
        bounding_box = new AABB(
                new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1,1));
    }

    public void update(float delta, Window window, Camera camera, Board board) {
        if (window.getDirection() == Direction.WEST)
            transform.pos.add(new Vector3f(-delta,0,0));
        if (window.getDirection() == Direction.EAST)
            transform.pos.add(new Vector3f(delta, 0, 0));
        if (window.getDirection() == Direction.NORTH)
            transform.pos.add(new Vector3f(0, delta, 0));
        if (window.getDirection() == Direction.SOUTH)
            transform.pos.add(new Vector3f(0, -delta, 0));

        bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
        AABB[] boxes = new AABB[25];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i + j * 5] = board.getTileBoundingBox(
                        (int)((transform.pos.x / 2 + 0.5f) - (5/2)) + i,
                        (int)((-transform.pos.y / 2 + 0.5f) - (5/2)) + j
                );
            }
        }
        AABB box = null;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null)
                    box = boxes[i];

                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
        if (box != null) {
            Collision data = bounding_box.getCollision(box);
            if (data.isIntersecting) {
                bounding_box.correctPosition(box, data);
                transform.pos.set(bounding_box.getCenter(),0);
                System.out.println("충돌발생!");
            }
        }

        camera.getPosition().lerp(transform.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
        //camera.setPosition(transform.pos.mul(-board.getScale(), new Vector3f()));
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        tex.bind(0);
        model.render();
    }
}
