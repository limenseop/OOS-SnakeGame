package src.domain;

import java.io.Serializable;

public class snakeBody implements Serializable {

    private float positionX;
    private float positionY;
    private Direction direction;

    public snakeBody(float positionX, float positionY, Direction direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction direction){
        this.direction = direction;
    }

    public void move(){
        switch (direction){
            case EAST:{
                positionX = positionX + 0.4f;
                break;
            }
            case WEST:{
                positionX = positionX - 0.4f;
                break;
            }
            case NORTH:{
                positionY = positionY + 0.4f;
                break;
            }
            case SOUTH:{
                positionY = positionY - 0.4f;
                break;
            }
        }
    }

    public void movePosition(float x, float y, Direction dir){
        this.positionX = x;
        this.positionY = y;
        this.direction = dir;
    }
}
