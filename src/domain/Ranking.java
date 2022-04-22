package src.domain;

import java.io.Serializable;

public class Ranking implements Serializable {

    private String id;
    private int score;

    public Ranking(String id, int score) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
}
