package src.board;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import src.collision.AABB;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.Window;

public class Board {
    private byte[] tiles;
    private int width, height, scale;
    private Matrix4f mainboard;
    private AABB[] bounding_boxes;
    private TileRenderer tilerenderer;
    public Board(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        tiles = new byte[width * height];
        bounding_boxes = new AABB[width*height];
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

    private void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            bounding_boxes[x + y * width]
                    = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
        }
        else {
            bounding_boxes[x + y * width]
                    = null;
        }
    }
    public void createBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) { 
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1)
                    setTile(Tile.test_tile2, i, j);
            }
        }
    }
    public AABB getTileBoundingBox(int x, int y) {
        try {
            return bounding_boxes[x+y*width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x+y*width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public int getScale() {return scale;};
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
