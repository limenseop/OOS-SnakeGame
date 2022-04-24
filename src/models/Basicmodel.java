package src.models;

public class Basicmodel extends Model {
    public Basicmodel() {
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
}
