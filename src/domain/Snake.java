package src.domain;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {

    snakeBody head;

    private List<snakeBody> body;

    private Direction direction;

    public snakeBody getHead() {
        return head;
    }

    public Snake(){
        body = new ArrayList<>();
        body.add(new snakeBody(20,20,Direction.NORTH));
        body.add(new snakeBody(20,19,Direction.NORTH));
        body.add(new snakeBody(20,18,Direction.NORTH));
        direction = Direction.NORTH;
        head = body.get(0);
    }

    public boolean grow(){

        snakeBody tail = body.get(body.size()-1);

        int x = (int)tail.getX();
        int y = (int)tail.getY();
        int new_X = 0;
        int new_Y = 0;

        switch(direction){
            case NORTH : {
                new_X = x;
                new_Y = y-1;
                break;
            }
            case SOUTH : {
                new_X = x;
                new_Y = y+1;
                break;
            }
            case EAST : {
                new_X = x+1;
                new_Y = y;
                break;
            }
            case WEST : {
                new_X = x-1;
                new_Y = y;
                break;
            }
        }
        snakeBody newBody = new snakeBody(new_X,new_Y,direction);
        body.add(newBody);
        return true;
    }

    public boolean change_Direction(Direction direction){
        if(this.direction == Direction.EAST && direction == Direction.WEST
            || this.direction == Direction.WEST && direction == Direction.EAST
            || this.direction == Direction.NORTH && direction == Direction.SOUTH
            || this.direction == Direction.SOUTH && direction == Direction.NORTH)
            return false;
        this.direction = direction;
        head.changeDirection(direction);
        return true;
    }

    public void move(){
        Direction prev = direction;
        Direction prev_buf = direction;
        for (snakeBody snakeBody : body) {
            snakeBody.move();
            prev_buf = snakeBody.getDirection();
            if(!snakeBody.equals(head)){
                snakeBody.changeDirection(prev);
            }
            prev = prev_buf;
        }
        if(check_If_collapse()){
            System.out.println("collapse occurs!");
        }
    }

    public boolean check_If_collapse(){
        Point headPoint = new Point(head.getX(),head.getY());
        for (snakeBody snakeBody : body) {
            if(snakeBody.equals(head)) continue;
            if(head.getX() == snakeBody.getX() && head.getY() == snakeBody.getY()){
                System.out.println("collapse occurs!!");
                return true;
            }
        }
        return false;
    }

    public boolean check_If_Overlap(int x, int y){
        for (snakeBody snakeBody : body) {
            if(x == snakeBody.getX() && y == snakeBody.getY())
                return true;
        }
        return false;
    }

    public void re_Init() {
        body.clear();
        System.out.println("Snake.setInit");
        body.add(new snakeBody(20,20,Direction.NORTH));
        body.add(new snakeBody(20,19,Direction.NORTH));
        body.add(new snakeBody(20,18,Direction.NORTH));
        head = body.get(0);
        System.out.println("this.head = " + this.head);
    }

    public List<Point> getBody(){
        List<Point> buffer = new ArrayList<>();
        for (snakeBody snakeBody : body) {
            buffer.add(snakeBody.getPoint());
        }
        return buffer;
    }
    public Direction getDirection() {
        return direction;
    }
}
