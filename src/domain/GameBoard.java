


package src.domain;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard implements Serializable {

    private final int MIN_X = 0;
    private final int MAX_X = 80;
    private final int MIN_Y = -82;
    private final int MAX_Y = -2;

    private String fileName = "user.acc";
    private String RankingFile = "Rank.acc";
    private List<Snake> snakes;
    private Snake snake;
    private Snake snake2;

    private List<Point2D> fruitPosition;
    private int score = 0;
    private boolean running = true;
    private boolean paused = false;
    private int player_num = 1;
    private transient List<Ranking> rankings;
    private String nickname;

    public GameBoard() {
        snakes = new ArrayList<>();
        snakes.add(new Snake(0));
        fruitPosition = new ArrayList<>();
        try {
            rankings = load_Ranking();
        }catch (Exception e){
            rankings = new ArrayList<>();
        }
        createFruit();
    }

    public void setDual_mode(){
        snakes.remove(0);
        player_num = 2;
        snakes.add(new Snake(1));
        snake2 = new Snake(2);
        snakes.add(snake2);
    }

    public void loadGame() throws Exception{
        GameBoard saveData = loadFromFile();
        this.snakes = saveData.getSnake();
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

            
            for (Snake sss : snakes) {

                while (sss.check_If_Overlap(fruit_X, fruit_Y)) {

                    fruit_X = (float) ((Math.random() * 78) + 2);
                    fruit_Y = (float) ((Math.random() * -78) - 2);

                }
                fruitPosition.add(new Point2D.Float(fruit_X, fruit_Y));
            }
        }
    }


    public synchronized boolean move_Snake() {
        for (Snake snake : snakes) {
            snake.move();
        }
        return true;

    }

    public boolean change_Direction_Snake(Direction direction,int idx) {

        if (snakes.get(idx).change_Direction(direction)) return true;
        else return false;

    }

    public boolean check_Game_Terminated() {

        for (Snake snake : snakes) {
            int headX = (int) snake.getHead().getPositionX();
            int headY = (int) snake.getHead().getPositionY();

            if (out_Of_Bounces(headX, headY) || snake.check_If_collapse()) {
                running = false;
                if(this.player_num == 1)recordRanking();
            }
        }
        return running;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean gameRunning(){
        return running;
    }

    public void re_Play(){
        running = true;
        score = 0;
        fruitPosition.clear();
        //snake.re_Init();

        //TODO : dual모드 구현 완료시 re_init해제하기
       createFruit();
    }

    public boolean check_Fruit_Overlap() {

        for (Snake snake : snakes) {
            Point2D fruit = fruitPosition.get(0);
            float headX = snake.getHead().getPositionX();
            float headY = snake.getHead().getPositionY();
            Point2D head = new Point2D.Float(headX, headY);
            if (head.distance(fruit) < 1.3) {
                fruitPosition.remove(0);
                score = score + 100;
                for (int i = 0; i < 8; i++)
                    snake.grow();
                createFruit();
                return true;
            }
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

    public List<Snake> getSnake() {
        return snakes;
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
        if (x < MIN_X || x >=MAX_X * player_num || y < MIN_Y || y >= MAX_Y) return true;
        return false;
    }

    private void recordRanking(){
        System.out.println("recorded!");
        rankings.add(new Ranking(nickname,score));
        sort_Ranking();
    }

    private void sort_Ranking(){
        Collections.sort(rankings,Collections.reverseOrder());
    }

}
