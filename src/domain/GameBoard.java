


package src.domain;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard implements Serializable {

    private final int MIN_X = 0;
    private final int MAX_X = 160;
    private final int MIN_Y = -164;
    private final int MAX_Y = -2;

    private String fileName = "user.acc";
    private String RankingFile = "Rank.acc";
    private List<Snake> snakes;

    private List<Point2D> fruitPosition;
    private List<Integer> scores;
    private int score = 0;
    private boolean running = true;
    private boolean paused = false;
    private boolean auto_dual = false;
    private int player_num = 1;
    private transient List<Ranking> rankings;
    private String nickname;

    private final AutoMover autoMover;
    public GameBoard() {
        snakes = new ArrayList<>();
        autoMover = new AutoMover(MAX_X, MIN_Y, 2.0f, 12.0f);
        snakes.add(new Snake(0));
        fruitPosition = new ArrayList<>();
        scores = new ArrayList<>();
        try {
            rankings = load_Ranking();
        }catch (Exception e){
            System.out.println("failed");
            rankings = new ArrayList<>();
        }
        createFruit();
    }

    public void set_Dual_mode(){
        scores.clear();
        scores.add(0);
        scores.add(0);
        System.out.println("paused = " + paused);
        snakes.clear();
        player_num = 2;
        snakes.add(new Snake(1));
        snakes.add(new Snake(2));
        auto_dual = true;
    }

    public void set_Auto_Mode(){
        auto_dual = true;
    }

    public void loadGame() throws Exception{
        GameBoard saveData = loadFromFile();
        this.snakes = saveData.getSnake();
        this.score = saveData.getScore();
        this.scores = saveData.getScores();
        this.paused = saveData.isPaused();
        this.running = saveData.isRunning();
        fruitPosition.remove(0);
        fruitPosition.add(saveData.getFruitPosition().get(0));
        auto_dual = false;
        player_num = 1;
    }

    public void createFruit() {

        int boundary_x = 70;
        int boundary_y = -70;

        double startTime = System.currentTimeMillis();

        System.out.println("player_num = " + player_num);
        float fruit_X = (float) ((Math.random() * boundary_x * player_num) + 2);
        float fruit_Y = (float) ((Math.random() * boundary_y * player_num) - 2);
        System.out.println(boundary_x);

            while (fruitPosition.size()<player_num) {
                for (Snake snake : snakes) {

                    while (snake.check_If_Overlap(fruit_X, fruit_Y)
                    ||(player_num == 2  && (!fruitPosition.isEmpty() && fruitPosition.get(0).distance(new Point2D.Float(fruit_X,fruit_Y))<1.3))) {

                        fruit_X = (float) ((Math.random() * boundary_x * player_num) + 2);
                        fruit_Y = (float) ((Math.random() * boundary_y * player_num) - 2);
                    }
                }
                fruitPosition.add(new Point2D.Float(fruit_X, fruit_Y));
            }
        //System.out.println("fruitPosition = " + fruitPosition.size());

        //System.out.println("(System.currentTimeMillis() - startTime) = " + (System.currentTimeMillis() - startTime));
    }

    public void moveAutoSnake() {
        Direction direction = autoMover.getNextDirection(snakes.get(0), snakes.get(1), getFruitPosition());
        change_Direction_Snake(direction, 0);
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

            if (out_Of_Bounces(headX, headY) || snake.check_If_collapse() || check_Snake_Crossed()) {
                running = false;
                if(this.player_num == 1 && auto_dual == false)recordRanking();
            }
        }
        return running;
    }

    public void auto_Move_Determination(){
        //solo모드로 가정 -> snake.0를 handle
        Snake target = snakes.get(0);
        Point2D snake_pos = new Point2D.Float(target.getHead().getPositionX(),target.getHead().getPositionY());
        Point2D selected_fruit = fruitPosition.get(0);
        boolean set_x;
        set_x = (Math.abs(snake_pos.getX()-selected_fruit.getX())>1.0);
        //System.out.println("set_x = " + set_x);
        //System.out.println("snake_pos = " + (snake_pos.getX() - selected_fruit.getX()));

        //1. 먼저 x축을 맞추기 위해 move
        if(set_x){
            System.out.println("x_setting");
            if(snake_pos.getX()>selected_fruit.getX()){
                target.change_Direction(Direction.WEST);
            }else{
                if(target.getDirection()!=Direction.EAST)
                target.change_Direction(Direction.EAST);
            }
        }
        else{
            System.out.println("y_setting");
            if(snake_pos.getY()> selected_fruit.getY()){
                target.change_Direction(Direction.SOUTH);
            }
            else{
                target.change_Direction(Direction.NORTH);
            }
        }

        //additional determination : 만약 새 fruit이 생성됬는데 같은 x축일경우?
        //-> 새 apple이 생성되었음을 인식할 수 있어야됨
        //score의 diff로 판단하자!

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean gameRunning(){
        return running;
    }

    public void re_Play(int num){
        running = true;
        score = 0;
        scores.clear();
        fruitPosition.clear();
        snakes.clear();
        if(num == 1) {
            scores.add(0);
            snakes.add(new Snake(0));
            player_num = 1;
            auto_dual = false;
        }
        else if(num == 2){
            set_Dual_mode();
            player_num = 2;
            auto_dual = true;
        }
       createFruit();
    }

    public boolean check_Fruit_Overlap() {

        double startTime = System.currentTimeMillis();

        int count = 0;
        for (Snake snake : snakes) {
            //TODO : 2개 fruit에 대해 check_overlap
            for(int s = 0;s< fruitPosition.size();s++) {
                Point2D fruit = fruitPosition.get(s);
                float headX = snake.getHead().getPositionX();
                float headY = snake.getHead().getPositionY();
                Point2D head = new Point2D.Float(headX, headY);
                if (head.distance(fruit) < 1.3) {
                    fruitPosition.remove(s);
                    score = score + 100;
                    System.out.println("count = " + count);
                    scores.set(count, scores.get(count) + 100);
                    for (int i = 0; i < 8; i++)
                        snake.grow();
                    createFruit();
                    //System.out.println("(System.currentTimeMillis() - startTime) = " + (System.currentTimeMillis() - startTime));
                    return true;
                }
            }
            count = count + 1;
        }
        //System.out.println("(System.currentTimeMillis() - startTime) = " + (System.currentTimeMillis() - startTime));
        return false;
    }

    public void save_This_Game() throws IOException {
        FileOutputStream fos=new FileOutputStream(fileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);
    }

    public void save_Ranking() throws IOException{
        if(player_num == 2) return;
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

    public List<Integer> getScores(){
        return scores;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getPlayer_num() {
        return player_num;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public boolean isAuto(){return auto_dual;}

    private GameBoard loadFromFile() throws Exception{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        GameBoard newboard = (GameBoard) ois.readObject();
        return newboard;
    }

    private List<Ranking> load_Ranking() throws IOException, ClassNotFoundException {
        System.out.println("i tried");
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

    private boolean check_Snake_Crossed() {
        if(player_num == 1) return false;
        else{
            Snake snake1 = snakes.get(0);
            snakeBody head1 = snake1.getHead();
            Snake snake2 = snakes.get(1);
            snakeBody head2 = snake2.getHead();
            boolean result = (snake1.check_If_Overlap(head2.getPositionX(), head2.getPositionY()) || snake2.check_If_Overlap(head1.getPositionX(), head1.getPositionY()));
            return result;
        }
    }
}
