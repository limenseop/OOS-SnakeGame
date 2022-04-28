package src.domain;

import src.entity.Entity;

import java.io.Serializable;

public class SaveData implements Serializable {

    private Snake snake;
    private int score = 0;
    private boolean running;
    private boolean paused;
    private float fruitX;
    private float fruitY;

    public SaveData(Snake snake, int score, boolean running, boolean paused, Entity fruit) {
        this.snake = snake;
        this.score = score;
        this.running = running;
        this.paused = paused;
        this.fruitX = fruit.getTransform().pos.x;
        this.fruitY = fruit.getTransform().pos.y;
    }

    public Snake getSnake() {
        return snake;
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

    public void reload(){
        snake.reLoad();
    }

    public Entity getFruit(){
        return new Entity(fruitX,fruitY,0);
    }
}
