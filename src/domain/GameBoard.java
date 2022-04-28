package src.domain;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Serializable {

    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;

    private String fileName = "user.acc";
    private Snake snake;
    private List<Point> fruitPosition;
    private List<Point> snakePoisition;
    private boolean[][] board = new boolean[WIDTH][HEIGHT];
    private int score = 0;
    private boolean running = true;
    private boolean paused = false;
    private List<Ranking> rankings;

    // board = 40 * 40 size (demo)

    public GameBoard(Snake snake) {
        this.snake = snake;
        fruitPosition = new ArrayList<>();
        rankings = new ArrayList<>();
        int fruit_X = (int) (Math.random() * 40);
        int fruit_Y = (int) (Math.random() * 40);
        fruitPosition.add(new Point(fruit_X, fruit_Y));
    }

    public void loadGame(){
        GameBoard newBoard = loadFromFile();
        this.snake = newBoard.snake;
        this.fruitPosition = newBoard.fruitPosition;
        this.snakePoisition = newBoard.snakePoisition;
        this.board = newBoard.board;
        this.score = newBoard.score;
        this.running = newBoard.running;
        this.paused = newBoard.paused;
        this.rankings = newBoard.rankings;

    }

    public synchronized void createFruit() {

        int fruit_X = (int) (Math.random() * 40);
        int fruit_Y = (int) (Math.random() * 40);

        if (fruitPosition.isEmpty()) {

            while (snake.check_If_Overlap(fruit_X, fruit_Y)) {

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
                if (snake.check_If_Overlap(x, y) || (fruitPosition.get(0).getX() == x && fruitPosition.get(0).getY() == y)) board[y][x] = true;
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

    public synchronized boolean move_Snake() {

        if(paused == true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        snake.move();
        update();
        return true;

    }

    public boolean change_Direction_Snake(Direction direction) {

        if (snake.change_Direction(direction)) return true;
        else return false;

    }

    public void check_Game_Terminated() {

        int headX = snake.getHead().getX();
        int headY = snake.getHead().getY();

        if (out_Of_Bounces(headX, headY) || snake.check_If_collapse()) {
            running = false;
        }
    }

    public boolean gameRunning(){
        return running;
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
        createFruit();
        update();
    }

    public boolean isPaused() {
        return paused;
    }

    public synchronized boolean switch_Pause() {
        if(paused == true){
            paused = false;
        }
        else if (paused == false) {
            paused = true;
        }
        return paused;
    }


    public void recordRanking(){
        rankings.add(new Ranking("hello",score));
    }

    public List<Ranking> showRanking(){
        return rankings;
    }


    public boolean check_Fruit_Overlap() {

        Point fruit = fruitPosition.get(0);
        Point head = new Point(snake.getHead().getX(),snake.getHead().getY());
        if(fruit.getX() == head.getX() && fruit.getY() == head.getY()){
            fruitPosition.remove(0);
            snake.grow();
            createFruit();
            return true;
        }
        return false;
    }

    private boolean out_Of_Bounces(int x, int y) {
        if (x < 0 || x >= 40 || y < 0 || y >= 40) return true;
        return false;
    }
    public void gameTerminate(){
        this.running =  false;
    }

    public void save_This_Game() throws IOException {
        FileOutputStream fos=new FileOutputStream(fileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);
    }

    private GameBoard loadFromFile(){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return(GameBoard)ois.readObject();
        }
        catch(Exception e) {
            System.out.println("error");
        }
        return null;
    }
}
