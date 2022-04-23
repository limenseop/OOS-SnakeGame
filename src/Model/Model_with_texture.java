package src.Model;

public class Model_with_texture extends Model {
    public Model_with_texture(float size) {
        super(new float[] {
                -size, size, 0,  //TOP LEFT 0
                size, size, 0,   //TOP RIGHT 1
                size, -size, 0,  //BOTTOM RIGHT 2
                -size, -size, 0, //BOTTOM LEFT 3
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
