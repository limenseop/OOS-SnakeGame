/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.board;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.models.Camera;
import src.models.Shader;

public class Board {
    private byte[] tiles;
    private final int view = 50;
    private int width, height, scale;
    private Matrix4f mainboard;

    public Board(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        tiles = new byte[width * height];
        mainboard = new Matrix4f()
                .setTranslation(new Vector3f(0,0,0))
                .scale(scale);
        createBoard();
    }

    public void render(TileRenderer render,Shader shader, Camera camera) {
        int posX = ((int)camera.getPosition().x + (camera.getWidth()/2)) / (scale * 2);
        int posY = ((int)camera.getPosition().y - (camera.getHeight()/2)) / (scale * 2);
        for (int i = 0; i < view; i++) {
            for (int j = 0 ; j < view; j++) {
                Tile t = getTile(i-posX, j+posY);
                if (t != null) {
                    render.renderTile(t, i-posX, -j-posY, shader, mainboard, camera);
                }
            }
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
    public void correctCameara(Camera camera) {
        Vector3f pos = camera.getPosition();
        int w = -width * scale * 2;
        int h = height * scale * 2;

        if (pos.x > -(camera.getWidth()/2) + scale)
            pos.x = -(camera.getWidth()/2) + scale;
        if (pos.x < w + (camera.getWidth()/2) + scale)
            pos.x = w + (camera.getWidth()/2) + scale;
        if (pos.y < (camera.getHeight()/2) - scale)
            pos.y = (camera.getHeight()/2) - scale;
        if (pos.y > h - (camera.getHeight()/2) - scale)
            pos.y = h - (camera.getHeight()/2) - scale;
    }
    private void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
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