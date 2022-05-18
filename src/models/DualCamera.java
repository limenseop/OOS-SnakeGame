package src.models;

import org.joml.Vector3f;

public class DualCamera extends Camera{

    private float viewPersent;
    public DualCamera(int width, int height) {
        super(width, height);
        viewPersent = 1;
    }
    public void setCam(Vector3f snake1_headpos, Vector3f snake2_headpos) {

    }
    private void setCamProjection() {

    }
}
