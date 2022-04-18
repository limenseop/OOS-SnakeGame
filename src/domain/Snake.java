package src.domain;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Snake {

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
        System.out.println("new_X = " + new_X);
        System.out.println("new_Y = " + new_Y);
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

    public List<snakeBody> getBody() {
        return body;
    }

    public void move(){
        System.out.println("head.getx() = " + head.getY());
        System.out.println("head.gety() = " + head.getX());
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

    public void brief(){
        int i = 0;
        for (snakeBody snakeBody : body) {
            i = i+1;
            System.out.println("point[" + i+ "]     x = " + snakeBody.getX() + "    y = " + snakeBody.getY() + "  direction : "  + snakeBody.getDirection());
        }
    }


}
