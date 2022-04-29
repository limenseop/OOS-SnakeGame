package src.models;

public class Menumodel extends Model {

    public Menumodel() {
        super(new float[] {
                -22f, 22f, 0,  //TOP LEFT 0
                22f, 22f, 0,   //TOP RIGHT 1
                22f, -22f, 0,  //BOTTOM RIGHT 2
                -22f, -22f, 0, //BOTTOM LEFT 3
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
