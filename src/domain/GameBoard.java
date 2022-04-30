package src.domain;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard implements Serializable {

    private String fileName = "user.acc";
    private String RankingFile = "Rank.acc";
    private Snake snake;

    private List<Point2D> fruitPosition;
    private int score = 0;
    private boolean running = true;
    private boolean paused = false;
    private transient List<Ranking> rankings;
    private String nickname;

    public GameBoard(Snake snake) {
        this.snake = snake;
        fruitPosition = new ArrayList<>();
        try {
            rankings = load_Ranking();
        }catch (Exception e){
            rankings = new ArrayList<>();
        }
        createFruit();
    }

    public void loadGame() throws Exception{
        GameBoard saveData = loadFromFile();
        this.snake = saveData.getSnake();
        this.score = saveData.getScore();
        this.paused = saveData.isPaused();
        this.running = saveData.isRunning();
        fruitPosition.remove(0);
        fruitPosition.add(saveData.getFruitPosition().get(0));
    }

    public void createFruit() {

        float fruit_X = (float) ((Math.random() * 78) + 2);
        float fruit_Y = (float) ((Math.random() * -78) - 2);

        if (fruitPosition.isEmpty()) {

            while (snake.check_If_Overlap(fruit_X, fruit_Y)) {

                fruit_X = (float) ((Math.random() * 78) + 2);
                fruit_Y = (float) ((Math.random() * -78) - 2);

            }
            fruitPosition.add(new Point2D.Float(fruit_X,fruit_Y));
        }
    }


    public synchronized boolean move_Snake() {
        snake.move();
        return true;

    }

    public boolean change_Direction_Snake(Direction direction) {

        if (snake.change_Direction(direction)) return true;
        else return false;

    }

    public boolean check_Game_Terminated() {

        int headX = (int)snake.getHead().getPositionX();
        int headY = (int)snake.getHead().getPositionY();

        if (out_Of_Bounces(headX, headY) || snake.check_If_collapse()) {
            running = false;
            recordRanking();
        }
        return running;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        System.out.println("this.nickname = " + this.nickname);
    }

    public boolean gameRunning(){
        return running;
    }

    public void re_Play(){
        running = true;
        score = 0;
        fruitPosition.clear();
        snake.re_Init();
        createFruit();
    }

    public boolean check_Fruit_Overlap() {

        Point2D fruit = fruitPosition.get(0);
        float headX = snake.getHead().getPositionX();
        float headY = snake.getHead().getPositionY();
        Point2D head = new Point2D.Float(headX,headY);
        if(head.distance(fruit)<1.3){
            fruitPosition.remove(0);
            score = score + 100;
            for(int i = 0;i<8;i++)
            snake.grow();
            createFruit();
            return true;
        }
        return false;
    }

    public void save_This_Game() throws IOException {
        FileOutputStream fos=new FileOutputStream(fileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);
    }

    public void save_Ranking() throws IOException{
        FileOutputStream fos=new FileOutputStream(RankingFile);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this.rankings);
    }

    public List<Point2D> getFruitPosition() {
        return fruitPosition;
    }

    public Snake getSnake() {
        return snake;
    }

    public void gameTerminate(){
        this.running =  false;
    }

    public int getScore() {
        return score;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public List<Ranking> getRankings() { return rankings; }

    public List<Ranking> showRanking(){
        return rankings;
    }



    private GameBoard loadFromFile() throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        GameBoard newboard = (GameBoard) ois.readObject();
        return newboard;
    }

    private List<Ranking> load_Ranking() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(RankingFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<Ranking> loaded_Ranking = (List<Ranking>) ois.readObject();
        return loaded_Ranking;
    }

    private boolean out_Of_Bounces(int x, int y) {
        if (x < 0 || x >=82 || y < -80 || y >= -2) return true;
        return false;
    }

    private void recordRanking(){
        System.out.println("recorded!");
        rankings.add(new Ranking(nickname,score));
        System.out.println("rankings = " + rankings.size());
        sort_Ranking();
    }

    private void sort_Ranking(){
        Collections.sort(rankings,Collections.reverseOrder());
    }

}
