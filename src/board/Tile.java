package src.board;

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static final Tile test_tile = new Tile((byte) 0, "bush");

    private byte id;
    private String texture;

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;
        if (tiles[id] != null) {
            throw new IllegalStateException("Board id["+id+"] is already being used.");
        }
        tiles[id] = this;
    }

    public String getTexture() {
        return texture;
    }
    public byte getId() {
        return id;
    }
}
