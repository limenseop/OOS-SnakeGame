package src.domain;

import java.io.Serializable;

public class Ranking implements Serializable,Comparable<Ranking>{

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

    @Override
    public int compareTo(Ranking o) {
        if(o.score<score){
            return 1;
        }
        else if(o.score>score){
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "id='" + id + '\'' +
                ", score=" + score +
                '}';
    }
}
