package src;

import src.board.Board;
import src.board.Tile;
import src.board.TileRenderer;
import src.entity.Player;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

public class SnakeGame {
    public static final int MAINWINDOWSIZE_WIDTH = 650;
    public static final int MAINWINDOWSIZE_HEIGHT = 650;
    public static final int MAXIMUM_FPS = 60;
    public static final String GAME_TITLE = "Snake Game Ver1.0";
    public static final float SNAKE_SPEED = 1.0f;

    public static void main(String[] args){
        Window mainwindow = new Window(MAINWINDOWSIZE_WIDTH, MAINWINDOWSIZE_HEIGHT, MAXIMUM_FPS, GAME_TITLE);
        mainwindow.setBackgroundcolor(0.0f, 0.0f, 0.0f);
        Shader shader = new Shader("shader");
        Camera cam = new Camera(mainwindow.getWidth(), mainwindow.getHeight());
        TileRenderer tiles = new TileRenderer();
        Board mainboard = new Board();
        Player snake = new Player(20,-20);
        for (int i = 0; i < 42; i++) {
            for (int j = 0; j < 42; j++) {
                if (i == 0 || j == 0 || i == 41 || j == 41)
                    mainboard.setTile(Tile.test_tile2, i, j);
            }
        }

        mainboard.correctCameara(cam, mainwindow);
        while(!mainwindow.close()){
            if (mainwindow.isUpdating()) {
                mainwindow.update();
                snake.update((float)mainwindow.getcurrentFps()*SNAKE_SPEED, mainwindow, cam, mainboard);
                mainboard.correctCameara(cam, mainwindow);


                mainboard.render(tiles, shader, cam);
                snake.render(shader, cam);
                mainwindow.swapBuffer();
            }
        }
        mainwindow.stop();
    }
}
