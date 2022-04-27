package src.board;

public class Tile {
    public static Tile tiles[] = new Tile[16];
    public static byte num_of_tile = 0;

    public static final Tile tile = new Tile("tile1");
    public static final Tile tile2 = new Tile("tile2").setSoild();
    public static final Tile apple = new Tile("appleontile").setEatable();
    private byte id;
    private String texture;
    private boolean solid;
    private boolean eatable;

    public Tile(String texture) {
        this.id = num_of_tile;
        this.texture = texture;
        this.solid = false;
        this.eatable = false;
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

    public Tile setEatable() {
        this.eatable = true;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }
    public boolean isEatable() {
        return eatable;
    }
    public String getTexture() {
        return texture;
    }
    public byte getId() {
        return id;
    }
}
