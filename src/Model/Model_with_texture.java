package src.Model;

public class Model_with_texture extends Model {
    public Model_with_texture(float size) {
        super(new float[] {
                -size, size, 0,
                size, size, 0,
                size, -size, 0,

                size, -size, 0,
                -size, -size, 0,
                -size, size, 0
        }, new float[] {
                0,0,
                1,0,
                1,1,

                1,1,
                0,1,
                0,0
        });
    }

}
