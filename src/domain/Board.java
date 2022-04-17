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

    // board = 40 * 40 size (demo)

    public Board(Snake snake){
        this.snake = snake;
        fruitPosition = new ArrayList<>();
    }

    public boolean createFruit(){

        int fruit_X = (int)(Math.random()*40);
        int fruit_Y = (int)(Math.random()*40);

        while(snake.check_If_Overlap(fruit_X,fruit_Y) && check_Fruit_Overlap(fruit_X,fruit_Y)){
            fruit_X = (int)(Math.random()*40);
            fruit_Y = (int)(Math.random()*40);
        }

        fruitPosition.add(new Point(fruit_X,fruit_Y));

        return true;
    }

    public void update(){
        for (int y = 0;y<HEIGHT;y++){
            for(int x = 0;x<WIDTH;x++){
                board[y][x] = false;
                if(snake.check_If_Overlap(y,x) || check_Fruit_Overlap(y,x)) board[y][x] = true;
            }
        }
    }

    public void brief(){
        List<String> buffer = new ArrayList<>();
        for (boolean[] booleans : board) {
            buffer.clear();
            for (boolean aBoolean : booleans) {
                if(aBoolean) buffer.add("o");
                else buffer.add(" ");
            }
            System.out.println(buffer);
        }
    }

    private boolean check_Fruit_Overlap(int x, int y){
        for (Point point : fruitPosition) {
            if(point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }


}
