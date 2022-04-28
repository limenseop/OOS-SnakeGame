package src.domain;

import org.joml.Vector2f;
import org.joml.Vector3f;
import src.board.Board;
import src.collision.AABB;
import src.collision.Collision;
import src.entity.Transform;
import src.models.Basicmodel;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;
import src.windowhandle.Window;

import java.awt.*;
import java.io.Serializable;

public class snakeBody implements Serializable {

    private transient Basicmodel model;
    private transient Texture tex;
    private Transform transform;
    private Direction direction;

    public snakeBody(float positionX, float positionY, Direction direction) {
        model = new Basicmodel();
        tex= new Texture("Cute-Snake-Transparent-PNG.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(positionX, positionY, 0);
        this.direction = direction;
    }

    public float getX() {
        return transform.pos.x;
    }

    public float getY() {
        return transform.pos.y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction direction){
        this.direction = direction;
    }

    public void move(){
        switch (direction){
            case EAST:{
                //positionX = positionX + 0.3f;
                transform.pos.add(0.3f,0,0);
                break;
            }
            case WEST:{
                //positionX = positionX - 0.3f;
                transform.pos.add(-0.3f,0,0);
                break;
            }
            case NORTH:{
                //positionY = positionY + 0.3f;
                transform.pos.add(0,0.3f,0);
                break;
            }
            case SOUTH:{
                //positionY = positionY - 0.3f;
                transform.pos.add(0,-0.3f,0);
                break;
            }
        }
    }

    public void movePosition(float x, float y, Direction dir){
        this.direction = dir;
        transform.pos.set(x,y,0);
    }



    public void focus(Camera camera,Board board){
        //현재 snakeBody로 camera focus시키기);
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
/**
 * package src.entity;
 *
 * import org.joml.Vector2f;
 * import org.joml.Vector3f;
 * import src.Direction;
 * import src.board.Board;
 * import src.collision.AABB;
 * import src.collision.Collision;
 * import src.models.Basicmodel;
 * import src.models.Camera;
 * import src.models.Shader;
 * import src.models.Texture;
 * import src.windowhandle.Window;
 *
 * public class Entity {
 *     private Basicmodel model;
 *     private Texture tex;
 *     private Transform transform;
 *     private AABB bounding_box;
 *     private Vector3f movement;
 *     private float delta;
 *
 *     public Entity(float x, float y, float delta) {
 *         model = new Basicmodel();
 *         tex= new Texture("Cute-Snake-Transparent-PNG.png");
 *         this.delta = delta;
 *         movement = new Vector3f(0, this.delta, 0);
 *         transform = new Transform();
 *         transform.scale = new Vector3f(16,16,1);
 *         transform.pos = new Vector3f(x, y, 0);
 *         bounding_box = new AABB(
 *                 new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1,1));
 *     }
 *     public void update(Window window, Camera camera, Board board) {
 *         //움직임
 *         if (window.getDirection() == Direction.WEST)
 *             movement = new Vector3f(-delta,0,0);
 *         if (window.getDirection() == Direction.EAST)
 *             movement = new Vector3f(delta,0,0);
 *         if (window.getDirection() == Direction.NORTH)
 *             movement = new Vector3f(0,delta,0);
 *         if (window.getDirection() == Direction.SOUTH)
 *             movement = new Vector3f(0,-delta,0);
 *         transform.pos.add(movement);
 *
 *         //충돌박스 생성 및 충돌인식
 *         bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
 *         AABB[] boxes = new AABB[25];
 *         for (int i = 0; i < 5; i++) {
 *             for (int j = 0; j < 5; j++) {
 *                 boxes[i + j * 5] = board.getTileBoundingBox(
 *                         (int)((transform.pos.x / 2 + 0.5f) - (5/2)) + i,
 *                         (int)((-transform.pos.y / 2 + 0.5f) - (5/2)) + j
 *                 );
 *             }
 *         }
 *         AABB box = null;
 *         for (int i = 0; i < boxes.length; i++) {
 *             if (boxes[i] != null) {
 *                 if (box == null)
 *                     box = boxes[i];
 *
 *                 Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
 *                 Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
 *
 *                 if (length1.lengthSquared() > length2.lengthSquared()) {
 *                     box = boxes[i];
 *                 }
 *             }
 *         }
 *         if (box != null) {
 *             Collision data = bounding_box.getCollision(box);
 *             if (data.isIntersecting) {
 *                 bounding_box.correctPosition(box, data);
 *                 transform.pos.set(bounding_box.getCenter(),0);
 *                 System.out.println("충돌발생!");
 *             }
 *         }
 *         camera.getPosition().lerp(transform.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
 *         //camera.setPosition(transform.pos.mul(-board.getScale(), new Vector3f()));
 *     }
 *
 *     public Transform getTransform() {
 *         return transform;
 *     }public void render(Shader shader, Camera camera) {
 *  *         shader.bind();
 *  *         shader.setUniform("sampler", 0);
 *  *         shader.setUniform("projection", transform.getProjection(camera.getProjection()));
 *  *         tex.bind(0);
 *  *         model.render();
 *  *     }
 *
 *
 * }
 */