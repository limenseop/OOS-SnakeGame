package src.domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;

    private Snake snake;
    private List<Point> fruitPosition;
    private boolean[][] board = new boolean[WIDTH][HEIGHT];
    private int score = 0;

    // board = 40 * 40 size (demo)

    public Board(Snake snake){
        this.snake = snake;
        fruitPosition = new ArrayList<>();
    }

    public boolean createFruit(){

        int fruit_X = (int)(Math.random()*40);
        int fruit_Y = (int)(Math.random()*40);

        while(snake.check_If_Overlap(fruit_X,fruit_Y) && check_Fruit_Overlap(fruit_X,fruit_Y,false)){
            fruit_X = (int)(Math.random()*40);
            fruit_Y = (int)(Math.random()*40);
        }

        fruitPosition.add(new Point(fruit_X,fruit_Y));

        return true;
    }

    public void update(){
        int head_X = snake.getHead().getX();
        int head_Y = snake.getHead().getY();
        for (int y = 0;y<HEIGHT;y++){
            for(int x = 0;x<WIDTH;x++){
                board[y][x] = false;
                if(snake.check_If_collapse() || out_Of_Bounces(head_X,head_Y)){
                    System.out.println("terminate game!");
                }
                if(snake.check_If_Overlap(x,y) || check_Fruit_Overlap(y,x,false)) board[y][x] = true;
            }
        }
    }

    public void brief(){
        List<String> buffer = new ArrayList<>();
        int i = 0;
        /*for (boolean[] booleans : board) {
            i = i+1;
            buffer.clear();
            buffer.add(i +"");
            for (boolean aBoolean : booleans) {
                if(aBoolean) buffer.add("o");
                else buffer.add(" ");
            }
            System.out.println(buffer);
        }*/

        for (int y= 0;y<HEIGHT;y++) {
            i = i + 1;
            buffer.clear();
            buffer.add(i + "");
            for (int x = 0; x < WIDTH; x++) {
                if (board[y][x]) {
                    if (y == snake.getHead().getY() && x == snake.getHead().getX()) {
                        buffer.add("*");
                    } else
                        buffer.add("o");
                } else buffer.add(" ");
            }
            System.out.println("buffer = " + buffer);
        }


        Point checkhead = new Point(snake.getHead().getY(),snake.getHead().getX());
    }

    public boolean move_Snake(){
        snake.move();
        int headx = snake.getHead().getX();
        int heady = snake.getHead().getY();
        if(check_Fruit_Overlap(heady,headx,true)){
            snake.grow();
            score = score + 100;
        }
        if(out_Of_Bounces(heady,headx)) return false;
        update();
        return true;
    }

    public boolean change_Direction_Snake(Direction direction){
        if(snake.change_Direction(direction)) return true;
        else return false;
    }

    private boolean check_Fruit_Overlap(int x, int y,boolean option){
        for (Point point : fruitPosition) {
            if(point.getX() == x && point.getY() == y) {
                if(option)
                fruitPosition.remove(point);
                return true;
            }
        }
        return false;
    }

    private boolean out_Of_Bounces(int x, int y){
        if(Math.abs(x)>=39 || Math.abs(y)>=39) return true;
        return false;
    }


}
