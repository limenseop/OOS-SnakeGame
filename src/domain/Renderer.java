package src.domain;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.board.Board;
import src.entity.Transform;
import src.models.*;

import java.util.List;

public class Renderer {

    Transform transform;
    private Texture SnakeTex;
    private Texture appleTex;
    private Texture mainmenuTex;
    private Texture SnakebodyTex;
    private Basicmodel model;
    private Snakemodel snakemodel;
    private List<snakeBody> snake;
    private snakeBody head;
    private Transform fruitPosition;
    private Menumodel meunmodel;

    public Renderer() {
        SnakeTex = new Texture("New Piskel.png");
        appleTex = new Texture("apple.png");
        SnakebodyTex = new Texture("SnakeGame_SnakeBody.png");
        mainmenuTex = new Texture("Pause_Buttons.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(0,0,0);
        fruitPosition = new Transform();
        fruitPosition.scale = new Vector3f(16,16,1);
        fruitPosition.pos = new Vector3f(0,0,0);
        model = new Basicmodel();
        snakemodel = new Snakemodel();
        meunmodel = new Menumodel();
    }

    public void setBoard(GameBoard board){
        this.snake = board.getSnake().getBody();
        head = board.getSnake().getHead();
        fruitPosition.pos.set((float) board.getFruitPosition().get(0).getX(), (float) board.getFruitPosition().get(0).getY(),0);
    }

    public void render(Shader shader, Camera camera,Board board, Direction direction) {
        setFocus(camera,board);
        transform.pos.set(snake.get(0).getPositionX(),snake.get(0).getPositionY(),0);
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        SnakeTex.bind(0);
        snakemodel.spin(direction);
        snakemodel.render();
        for (int i = 3; i < snake.size(); i++) {
            transform.pos.set(snake.get(i).getPositionX(), snake.get(i).getPositionY(), 0);
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(camera.getProjection()));
            SnakebodyTex.bind(0);
            model.render();
        }
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", fruitPosition.getProjection(camera.getProjection()));
        appleTex.bind(0);
        model.render();
    }
    public void mainmenurender(Shader shader, Board mainboard) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", new Matrix4f().setOrtho2D(-mainboard.getWidth()/2, mainboard.getWidth()/2, -mainboard.getHeight()/2,mainboard.getHeight()/2));
        mainmenuTex.bind(0);
        meunmodel.render();
    }

    private void setFocus(Camera camera, Board board){
        Transform focus = new Transform();
        focus.scale = new Vector3f(16);
        focus.pos = new Vector3f((float) head.getPositionX(), (float) head.getPositionY(),0);
        //camera.getPosition().lerp(focus.pos.mul(-board.getScale(), new Vector3f()), 1.0f);
        camera.setPosition(focus.pos.mul(-board.getScale(), new Vector3f()));
    }
}