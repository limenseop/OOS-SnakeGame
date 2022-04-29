package src.models;

import src.domain.Direction;

public class Snakemodel extends Model {
    private float[] tex_coord = new float[]{
            0, 0,
            1, 0,
            1, 1,
            0, 1
    };
    public Snakemodel() {
        super(new float[] {
                -1f, 1f, 0,  //TOP LEFT 0
                1f, 1f, 0,   //TOP RIGHT 1
                1f, -1f, 0,  //BOTTOM RIGHT 2
                -1f, -1f, 0, //BOTTOM LEFT 3
        }, new float[] {
                0,0,
                1,0,
                1,1,
                0,1
        }, new int[] {
                0, 1, 2,
                2, 3, 0
        });
    }

    public void spin(Direction direction) {
        if (direction == Direction.WEST) {
            tex_coord = new float[]{
                    1, 0,
                    1, 1,
                    0, 1,
                    0, 0
            };
        } else if (direction == Direction.EAST) {
            tex_coord = new float[]{
                    0, 1,
                    0, 0,
                    1, 0,
                    1, 1
            };
        } else if (direction == Direction.NORTH) {
            tex_coord = new float[]{
                    0, 0,
                    1, 0,
                    1, 1,
                    0, 1
            };
        } else if (direction == Direction.SOUTH) {
            tex_coord = new float[]{
                    1, 1,
                    0, 1,
                    0, 0,
                    1, 0
            };
        }
        super.spin(tex_coord);
    }
}
