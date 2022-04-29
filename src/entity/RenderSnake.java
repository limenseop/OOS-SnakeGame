package src.entity;

import src.domain.Direction;
import src.models.Camera;
import src.models.Shader;
import src.board.Board;

import java.awt.*;
import java.util.List;

public class RenderSnake {
    private Entity_head head;
    private Board mainboard;
    private Camera camera;
    private Shader shader;
    private List<Entity> bodylist;
    private int bodylength;
    public RenderSnake(int boardwidth, int boardheigth, int bodylength) {
        mainboard = new Board(boardwidth, boardheigth);
        camera = new Camera(boardwidth, boardheigth);
        shader = new Shader("shader");
        head = new Entity_head((float)2*boardwidth, -(float)2*boardheigth);
        this.bodylength = bodylength;
        bodylist.add(head);
        for (int i = 1; i < this.bodylength; i++)
            bodylist.add(new Entity_body(-20,20));
    }

    public void update(Direction direction, List<Point>snakepos) {
        head.update(direction, snakepos.get(0), camera, mainboard);
        int possize = snakepos.size();
        if (possize > bodylength)
            bodylist.add(new Entity_body(-20, 20));
        else {
            for (int i = 1; i < possize; i++)
                bodylist.get(i).update(snakepos.get(i), camera, mainboard);
        }
    }
    public void renderall() {
        for (Entity ent : bodylist) {
            ent.render(shader, camera);
        }
    }
}
