package src.board;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.models.*;
import src.models.Shader;
import src.windowhandle.Window;

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
    }
    public void render(Shader shader, Camera camera) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tilerenderer.renderTile(tiles[j + i * width], j, -i, shader, mainboard, camera);
            }
        }
    }
    public void correctCameara(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();
        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(window.getWidth()/2) + scale)
            pos.x = -(window.getWidth()/2) + scale;
        if (pos.x < w + (window.getWidth()/2) + scale)
            pos.x = w + (window.getWidth()/2) + scale;

        if (pos.y < (window.getHeight()/2) - scale)
            pos.y = (window.getHeight()/2) - scale;
        if (pos.y > h - (window.getHeight()/2) - scale)
            pos.y = h - (window.getHeight()/2) - scale;
    }
    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
    }

    public int getScale() {return scale;}
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}
