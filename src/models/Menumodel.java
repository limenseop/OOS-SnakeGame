package src.models;

public class Menumodel extends Model {

    public Menumodel() {
        super(new float[] {
                -20f, 20f, 0,  //TOP LEFT 0
                20f, 20f, 0,   //TOP RIGHT 1
                20f, -20f, 0,  //BOTTOM RIGHT 2
                -20f, -20f, 0, //BOTTOM LEFT 3
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
