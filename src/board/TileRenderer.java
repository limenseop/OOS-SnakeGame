/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.board;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.models.*;

import java.util.HashMap;

public class TileRenderer {
    private HashMap<String, Texture> tile_textures;
    private Basicmodel model;

    public TileRenderer() {
        tile_textures = new HashMap<String, Texture>();
        model = new Basicmodel();

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!tile_textures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tile_textures.put(tex, new Texture(tex+".png"));
                }
            }
        }
    }
    public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
        shader.bind();
        if (tile_textures.containsKey(tile.getTexture()))
            tile_textures.get(tile.getTexture()).bind(0);

        Matrix4f tile_position = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
        Matrix4f target = new Matrix4f();

        camera.getProjection().mul(world, target);
        target.mul(tile_position);
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);
        model.render();
    }
}