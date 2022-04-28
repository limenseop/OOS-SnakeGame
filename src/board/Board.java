package src.board;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.models.Camera;
import src.models.Shader;

public class Board {
    private byte[] tiles;
    private int width, height, scale;
    private Matrix4f mainboard;
    private TileRenderer tilerenderer;
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = 16;
        tiles = new byte[width * height];
        tilerenderer = new TileRenderer();
        mainboard = new Matrix4f()
                .setTranslation(new Vector3f(0,0,0))
                .scale(scale);
        createBoard();
    }
    public void render(Shader shader, Camera camera) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tilerenderer.renderTile(tiles[j + i * width], j, -i, shader, mainboard, camera);
            }
        }
    }
    private void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
    }
    public void createBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) { 
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1)
                    setTile(Tile.tile2, i, j);
            }
        }
    }
    public int getScale() {return scale;}
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}
