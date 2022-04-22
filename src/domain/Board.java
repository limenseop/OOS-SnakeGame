package src.domain;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;

    private Snake snake;
    private List<Point> fruitPosition;
    private List<Point> snakePoisition;
    private boolean[][] board = new boolean[WIDTH][HEIGHT];
    private int score = 0;
    private boolean running = true;
    private List<Ranking> rankings;

    // board = 40 * 40 size (demo)

    public Board(Snake snake) {
        this.snake = snake;
        fruitPosition = new ArrayList<>();
        rankings = new ArrayList<>();
        int fruit_X = (int) (Math.random() * 40);
        int fruit_Y = (int) (Math.random() * 40);
        fruitPosition.add(new Point(fruit_X, fruit_Y));
    }

    public synchronized void createFruit() {

        int fruit_X = (int) (Math.random() * 40);
        int fruit_Y = (int) (Math.random() * 40);
        if (fruitPosition.isEmpty()) {

            while (snake.check_If_Overlap(fruit_X, fruit_Y) && check_Fruit_Overlap(fruit_X, fruit_Y, false)) {

                fruit_X = (int) (Math.random() * 40);
                fruit_Y = (int) (Math.random() * 40);

            }

            fruitPosition.add(new Point(fruit_X, fruit_Y));
        }
    }

    public synchronized void update() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[y][x] = false;
                if (snake.check_If_Overlap(x, y) || check_Fruit_Overlap(y, x, false)) board[y][x] = true;
            }
        }
        snakePoisition = snake.getBody();
    }

    public void brief() {

        List<String> buffer = new ArrayList<>();
        int i = 0;

        for (int y = 0; y < HEIGHT; y++) {
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
        Point checkhead = new Point(snake.getHead().getY(), snake.getHead().getX());
    }

    public boolean move_Snake() {

        snake.move();
        int headx = snake.getHead().getX();
        int heady = snake.getHead().getY();

        if (check_Fruit_Overlap(heady, headx, true)) {
            snake.grow();
            score = score + 100;
            fruitPosition.clear();
            createFruit();
        }
        if (out_Of_Bounces(headx, heady)) return false;
        update();
        return true;
    }

    public boolean change_Direction_Snake(Direction direction) {
        if (snake.change_Direction(direction)) return true;
        else return false;
    }

    public synchronized boolean gameTermination() {

        int headX = snake.getHead().getX();
        int headY = snake.getHead().getY();

        if (out_Of_Bounces(headX, headY) || snake.check_If_collapse()) {
            return true;
        }
        return false;
    }

    public void re_Play(){
        score = 0;
        for (boolean[] booleans : board) {
            for (boolean cell : booleans) {
                cell = false;
            }
        }
        fruitPosition.clear();
        snake.re_Init();
        update();
        createFruit();
    }

    public synchronized boolean gamePause() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return running;
    }

    public synchronized boolean switch_Pause() {
        if(running == true){
            running = false;
        }
        else if (running == false) {
            notifyAll();
            running = true;
        }
        return running;
    }

    public boolean getRunning(){
        return running;
    }


    public void recordRanking(){
        rankings.add(new Ranking("hello",score));
    }

    public List<Point> getSnake(){
        return snakePoisition;
    }

    public List<Point> getFruit(){
        return fruitPosition;
    }

    public List<Ranking> showRanking(){
        return rankings;
    }


    private boolean check_Fruit_Overlap(int x, int y, boolean option) {
        for (Point point : fruitPosition) {
            if (point.getX() == x && point.getY() == y) {
                if (option)
                    fruitPosition.remove(point);
                return true;
            }
        }
        return false;
    }

    private boolean out_Of_Bounces(int x, int y) {
        if (x < 0 || x >= 40 || y < 0 || y >= 40) return true;
        return false;
    }
}
