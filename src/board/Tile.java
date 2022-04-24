package src.board;

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static byte num_of_tile = 0;

    public static final Tile test_tile = new Tile("tile1");
    public static final Tile test_tile2 = new Tile("tile2").setSoild();
    private byte id;
    private String texture;
    private boolean solid;

    public Tile(String texture) {
        this.id = num_of_tile;
        this.texture = texture;
        this.solid = false;
        num_of_tile++;
        if (tiles[id] != null) {
            throw new IllegalStateException("Board id["+id+"] is already being used.");
        }
        tiles[id] = this;
    }
    public Tile setSoild() {
        this.solid = true;
        return this;
    }
    public boolean isSolid() {
        return solid;
    }
    public String getTexture() {
        return texture;
    }
    public byte getId() {
        return id;
    }
}
