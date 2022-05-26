package src.models;

import org.joml.Vector3f;
import src.domain.snakeBody;

public class DualCamera extends Camera{
    private float boardsize;
    public DualCamera(int width, int height, float boardsize) {
        super(width, height);
        this.boardsize = boardsize;
    }
    public void setDualProjection(snakeBody fstSnakeHead, snakeBody secSnakeHead) {
        double vsize = getDistance(fstSnakeHead, secSnakeHead)/boardsize + 1.0;
        setViewsize(vsize);
    }
    private double getDistance(snakeBody fstSnake, snakeBody secSnake) {
        double xdis = Math.pow((fstSnake.getPositionX() - secSnake.getPositionX()),2);
        double ydis = Math.pow((fstSnake.getPositionY() - secSnake.getPositionY()),2);
        return Math.sqrt(xdis + ydis);
    }
    public Vector3f getCenter(snakeBody fstSnake, snakeBody secSnake) {
        float xpos = (fstSnake.getPositionX() + secSnake.getPositionX())/2;
        float ypos = (fstSnake.getPositionY() + secSnake.getPositionY())/2;
        return new Vector3f(xpos, ypos, 0);
    }
}
