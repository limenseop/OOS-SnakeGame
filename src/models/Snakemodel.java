package src.models;

public class Snakemodel extends Model {
    public Snakemodel() {
        super(new float[] {
                -1.25f, 1.25f, 0,  //TOP LEFT 0
                1.25f, 1.25f, 0,   //TOP RIGHT 1
                1.25f, -1.25f, 0,  //BOTTOM RIGHT 2
                -1.25f, -1.25f, 0, //BOTTOM LEFT 3
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
}