package src.domain;

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
    private Basicmodel model;
    private List<snakeBody> snake;
    private snakeBody head;
    private Transform fruitPosition;
    private Menumodel meunmodel;

    public Renderer() {
        SnakeTex = new Texture("Cute-Snake-Transparent-PNG.png");
        appleTex = new Texture("apple.png");
        mainmenuTex = new Texture("Background.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(0,0,0);
        fruitPosition = new Transform();
        fruitPosition.scale = new Vector3f(16,16,1);
        fruitPosition.pos = new Vector3f(0,0,0);
        model = new Basicmodel();
        meunmodel = new Menumodel();
    }

    public void setBoard(GameBoard board){
        this.snake = board.getSnake().getBody();
        head = board.getSnake().getHead();
        fruitPosition.pos.set((float) board.getFruitPosition().get(0).getX(), (float) board.getFruitPosition().get(0).getY(),0);
    }

    public void render(Shader shader, Camera camera,Board board) {
        setFocus(camera,board);
        for (snakeBody point : snake) {
            transform.pos.set(point.getPositionX(),point.getPositionY(),0);
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(camera.getProjection()));
            SnakeTex.bind(0);
            model.render();
        }
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", fruitPosition.getProjection(camera.getProjection()));
        appleTex.bind(0);
        model.render();
    }
    public void mainmenurender(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        mainmenuTex.bind(0);
        meunmodel.render();
    }


    private void setFocus(Camera camera, Board board){
        Transform focus = new Transform();
        focus.scale = new Vector3f(16,16,1);
        focus.pos = new Vector3f((float) head.getPositionX(), (float) head.getPositionY(),0);
        camera.getPosition().lerp(focus.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
    }
}
