package src.domain;

import org.joml.Vector3f;
import src.board.Board;
import src.entity.Entity;
import src.entity.Transform;
import src.models.Basicmodel;
import src.models.Camera;
import src.models.Shader;
import src.models.Texture;
import src.windowhandle.Window;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Serializable {

    private String fileName = "user.acc";
    private Snake snake;

    private List<Entity> fruitPosition;
    private int score = 0;
    private boolean running = true;
    private boolean paused = false;
    private List<Ranking> rankings;

    // board = 40 * 40 size (demo)

    public GameBoard(Snake snake) {
        this.snake = snake;
        fruitPosition = new ArrayList<>();
        rankings = new ArrayList<>();
        createFruit();
    }

    public void loadGame(){
        SaveData saveData = loadFromFile();
        this.snake = saveData.getSnake();
        this.score = saveData.getScore();
        this.paused = saveData.isPaused();
        this.running = saveData.isRunning();
        fruitPosition.remove(0);
        fruitPosition.add(saveData.getFruit());
    }

    public void createFruit() {

        float fruit_X = (float) ((Math.random() * 78) + 2);
        float fruit_Y = (float) ((Math.random() * -78) - 2);

        if (fruitPosition.isEmpty()) {

            while (snake.check_If_Overlap(fruit_X, fruit_Y)) {

                fruit_X = (float) ((Math.random() * 78) + 2);
                fruit_Y = (float) ((Math.random() * -78) - 2);

            }
            fruitPosition.add(new Entity(fruit_X,fruit_Y,0));
        }
    }

    public synchronized void update(Camera cam, Board brd) {
        snake.getHead().focus(cam,brd);
    }


    public synchronized boolean move_Snake() {
        snake.move();
        return true;

    }

    public boolean change_Direction_Snake(Direction direction) {

        if (snake.change_Direction(direction)) return true;
        else return false;

    }

    public void check_Game_Terminated() {

        int headX = (int)snake.getHead().getX();
        int headY = (int)snake.getHead().getY();

        if (out_Of_Bounces(headX, headY) || snake.check_If_collapse()) {
            running = false;
        }
    }

    public boolean gameRunning(){
        return running;
    }

    public void re_Play(){
        score = 0;
        fruitPosition.clear();
        snake.re_Init();
        createFruit();
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


    public boolean check_Fruit_Overlap(Window main) {

        long start = System.currentTimeMillis();
        Point2D fruit = new Point2D.Float(fruitPosition.get(0).getTransform().pos.x,fruitPosition.get(0).getTransform().pos.y);
        float headX = snake.getHead().getX();
        float headY = snake.getHead().getY();
        Point2D head = new Point2D.Float(headX,headY);
        if(head.distance(fruit)<1.3){
            fruitPosition.remove(0);
            score = score + 100;
            for(int i = 0;i<2;i++)
            snake.grow();
            createFruit();
            long interval = System.currentTimeMillis() - start;
            System.out.println("interval : " + interval);
            return true;
        }
        return false;
    }

    private boolean out_Of_Bounces(int x, int y) {
        if (x < 2.5 || x >= 81.5 || y < -81.5 || y >= -2.5) return true;
        return false;
    }
    public void gameTerminate(){
        this.running =  false;
    }

    public void save_This_Game() throws IOException {
        SaveData saved = new SaveData(snake,score,running,paused,fruitPosition.get(0));
        FileOutputStream fos=new FileOutputStream(fileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(saved);
    }

    private SaveData loadFromFile(){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SaveData saved = (SaveData) ois.readObject();
            saved.reload();
            return saved;
        }
        catch(Exception e) {
            System.out.println("error");
        }
        return null;
    }

    public void render(Shader shader, Camera cam){
        snake.render(shader,cam);
        for (Entity entity : fruitPosition) {
            entity.render(shader,cam);
        }
    }
}
