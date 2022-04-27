package src.domain;

import java.awt.*;
import java.io.Serializable;

public class snakeBody implements Serializable {

    private int positionX;
    private int positionY;
    private Direction direction;

    public snakeBody(int positionX, int positionY, Direction direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    public Point getPoint(){return new Point(positionX,positionY);}

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction direction){
        this.direction = direction;
    }

    public void move(){
        switch (direction){
            case EAST:{
                positionX = positionX - 1;
                break;
            }
            case WEST:{
                positionX = positionX + 1;
                break;
            }
            case NORTH:{
                positionY = positionY + 1;
                break;
            }
            case SOUTH:{
                positionY = positionY - 1;
                break;
            }
        }
    }
}