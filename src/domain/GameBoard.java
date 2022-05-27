


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

    private List<Point2D> fruitPosition;
    private List<Integer> scores;
    private boolean running = true;
    private boolean paused = false;
    private boolean auto_dual = false;
    private boolean is_auto = false;
    private int player_num = 1;
    private transient List<Ranking> rankings;
    private String nickname;

    private transient AutoMover autoMover;
    public GameBoard() {
        snakes = new ArrayList<>();
        autoMover = new AutoMover(MAX_X, MIN_Y, 1.5f, 12.0f);
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
        snakes.clear();
        player_num = 2;
        snakes.add(new Snake(1));
        snakes.add(new Snake(2));
        auto_dual = true;
    }

    public void set_Auto_Mode(){
        set_Dual_mode();
        is_auto = true;
    }

    public void loadGame() throws Exception{
        GameBoard saveData = loadFromFile();
        this.snakes = saveData.getSnake();
        this.scores = saveData.getScores();
        this.paused = saveData.isPaused();
        this.running = saveData.isRunning();
        this.nickname = saveData.getNickname();
        fruitPosition.remove(0);
        fruitPosition.add(saveData.getFruitPosition().get(0));
        auto_dual = false;
        is_auto = false;
        autoMover = new AutoMover(MAX_X, MIN_Y, 1.5f, 12.0f);
        player_num = 1;
    }

    public void createFruit() {

        int boundary_x = 70;
        int boundary_y = -70;


        float fruit_X = (float) ((Math.random() * boundary_x * player_num) + 2);
        float fruit_Y = (float) ((Math.random() * boundary_y * player_num) - 2);

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
                if(auto_dual == false)recordRanking();
            }
        }
        return running;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public boolean gameRunning(){
        return running;
    }

    public void re_Play(int num){
        running = true;
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

        int count = 0;
        for (Snake snake : snakes) {
            for(int s = 0;s< fruitPosition.size();s++) {
                Point2D fruit = fruitPosition.get(s);
                float headX = snake.getHead().getPositionX();
                float headY = snake.getHead().getPositionY();
                Point2D head = new Point2D.Float(headX, headY);
                if (head.distance(fruit) < 1.3) {
                    fruitPosition.remove(s);
                    scores.set(count, scores.get(count) + 100);
                    for (int i = 0; i < 8; i++)
                        snake.grow();
                    createFruit();
                    return true;
                }
            }
            count = count + 1;
        }
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

    public boolean isAutoDual(){return auto_dual;}

    public boolean isAuto(){
        return is_auto;
    }

    public int ranking(){
        int idx = 0;
        for (Ranking ranking : rankings) {
            if(scores.get(0) >= ranking.getScore()) return idx+1;
            idx = idx + 1;
        }
        return 1;
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
        rankings.add(new Ranking(nickname,scores.get(0)));
        System.out.println("rankings = " + rankings);
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
