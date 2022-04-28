package src.board;

import org.joml.Vector3f;
import org.joml.Vector3i;
import src.entity.Entity;

import java.awt.*;


public class AppleBuilder {

    private int width;
    private int height;
    private Vector3i fruitPosition;
    private boolean flag;

    public AppleBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        fruitPosition = new Vector3i();
        flag = true;
    }

    public void createFruit(Entity snake) {

        int fruit_X = (int)(Math.random() * (width-1)) + 1;
        int fruit_Y = (int)(Math.random() * (height-1)) + 1;

        if (flag) {
            while (check_If_Overlap(fruit_X, fruit_Y, snake)) {
                fruit_X = (int)(Math.random() * (width-1)) + 1;
                fruit_Y = (int)(Math.random() * (height-1)) + 1;
            }
            fruitPosition = new Vector3i(fruit_X, fruit_Y, 0);
        }
        flag = false;
    }

    public boolean check_If_Overlap(int x, int y, Entity snake){
        //for (snakeBody snakeBody : body) {
            if(Math.abs(snake.getTransform().pos.x - (float)2*x) <= 1 &&
                    Math.abs(snake.getTransform().pos.y + (float)2*y) <= 1
            )
                return true;
        //}
        return false;
    }
    protected void changeFlag() {
        flag = !flag;
    }
    public Vector3i getFruitPosition() {
        return fruitPosition;
    }
}
