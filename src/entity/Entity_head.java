package src.entity;

import org.joml.Vector3f;
import src.domain.Direction;
import src.board.Board;
import src.models.Shader;
import src.models.Camera;
import java.awt.*;

public class Entity_head extends Entity {
    private float[] tex_coord;

    public Entity_head(float x, float y) {
        super(x, y, "Snakehead.png");
        tex_coord = new float[] {
                0,0,
                1,0,
                1,1,
                0,1
        };
    }
    public void update(Direction direction, Point position, Camera camera, Board board) {
        if (direction == Direction.WEST) {
            tex_coord = new float[]{
                    1, 0,
                    1, 1,
                    0, 1,
                    0, 0
            };
        } else if (direction == Direction.EAST) {
            tex_coord = new float[]{
                    0, 1,
                    0, 0,
                    1, 0,
                    1, 1
            };
        } else if (direction == Direction.NORTH) {
            tex_coord = new float[]{
                    0, 0,
                    1, 0,
                    1, 1,
                    0, 1
            };
        } else if (direction == Direction.SOUTH) {
            tex_coord = new float[]{
                    1, 1,
                    0, 1,
                    0, 0,
                    1, 0
            };
        }
        super.update(position);
        camera.getPosition().lerp(transform.pos.mul(-board.getScale(), new Vector3f()), 1.0f);
    }
    @Override
        public void render(Shader shader, Camera camera) {
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(camera.getProjection()));
            tex.bind(0);
            model.spin(tex_coord);
            model.render();
        }
}
