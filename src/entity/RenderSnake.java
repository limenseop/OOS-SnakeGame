package src.entity;

import src.board.Tile;
import src.domain.Direction;
import src.models.*;
import src.board.Board;
import src.windowhandle.Window;

import java.awt.*;
import java.util.List;

public class RenderSnake {
    private Entity_head head;
    private Board mainboard;
    private Camera camera;
    private Shader shader;
    private Entity[] bodylist;
    private Point fruitpos;
    private int bodylength;
    public RenderSnake(int boardwidth, int boardheight, int windowwidth, int windowheight) {
        mainboard = new Board(boardwidth, boardheight);
        camera = new Camera(windowwidth, windowheight);
        shader = new Shader("shader");
        head = new Entity_head((float)boardwidth, -(float)boardheight);
        bodylist = new Entity[100];
        this.bodylength = 1;
        this.fruitpos = new Point(1, 1);
        bodylist[0] = head;
        for (int i = 1; i <= 2; i++)
            bodylist[i] = new Entity_body((float)(boardwidth + i), -(float)(boardheight + i));
    }

    public void update(Direction direction, List<Point>snakepos, List<Point> fruitpos, Window window) {
        if (!fruitpos.isEmpty()) {
            if (!this.fruitpos.equals(fruitpos.get(0))) {
                mainboard.setTile(Tile.tile, this.fruitpos.x, this.fruitpos.y);
                this.fruitpos.setLocation(fruitpos.get(0));
                mainboard.setTile(Tile.apple, this.fruitpos.x, this.fruitpos.y);

            }
        }
        if (!snakepos.isEmpty()) {
            head.update(direction, snakepos.get(0), camera, mainboard);
            int possize = snakepos.size();
            if (possize > bodylength) {
                bodylist[possize] = new Entity_body(10,10);
                bodylength = possize;
            }
            else {
                for (int i = 1; i < possize; i++)
                    bodylist[i].update(snakepos.get(i));
            }
        }
        mainboard.correctCameara(camera, window);
    }
    public void renderall() {
        mainboard.render(shader, camera);
        for (int i = 0; i < bodylength; i++)
            bodylist[i].render(shader, camera);
    }
}
