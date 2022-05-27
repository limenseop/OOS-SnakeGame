package src.domain;

import java.awt.geom.Point2D;
import java.util.List;

public class AutoMoveHandler {
    //auto moving시 필요한 state정보를 저장하는 곳

    private Snake auto_snake;
    private snakeBody head;
    private Point2D head_point;
    private Point2D fruit_point;

    private Direction dir_x;
    private Direction dir_y;
    private Direction prev_x = Direction.EAST;
    private Direction prev_y = Direction.NORTH;

    private boolean move_danger = false;
    private boolean switch_set = true;

    private float max_x;
    private float max_y;

    private float min_x;
    private float min_y;

    boolean flag_x = false;
    boolean flag_y = false;

    public AutoMoveHandler(Snake snake){
        this.auto_snake = snake;
        head = auto_snake.getHead();
        head_point = new Point2D.Float(auto_snake.getHead().getPositionX(),auto_snake.getHead().getPositionY());
    }

    private void calculate_Danger_Box(){
        //현재 snake의 분포를 바탕으로 dangerbox를 계산
        List<snakeBody> bodies = auto_snake.getBody();
        max_x = 0;
        min_x = 99999;
        max_y = 0;
        min_y = 99999;
        for (snakeBody body : bodies) {
            float body_x = body.getPositionX();
            float body_y = Math.abs(body.getPositionY());
            if(body_x>max_x) max_x = body_x;
            if(body_x<min_x) min_x = body_x;
            if(body_y>max_y) max_y = body_y;
            if(body_y<min_y) min_y = body_y;
        }
        max_y = max_y * -1;
        min_y = min_y * -1;
    }

    public void set_Fruit(Point2D fruit){
        this.fruit_point = fruit;
    }

    private void is_Danger(Point2D pos){
        if(flag_x==false || flag_y == false){
            move_danger = false;
            return;
        }
        if((pos.getX()>min_x && pos.getX()<max_x) != (pos.getY()>min_y && pos.getY()<max_y)) {
            head_point.setLocation(head.getPositionX(),head.getPositionY());
            move_danger = true;
            return;
        }
        if(acrossing_Danger_x(dir_x)) {
            move_danger = true;
            return;
        }
        move_danger = false;
        determine_NextDir(fruit_point);
    }

    public void determine_NextDir(Point2D fruitpos) {
        head_point.setLocation(head.getPositionX(),head.getPositionY());
        calculate_Danger_Box();
        if(head_point.getX()>fruitpos.getX()) {
            check_Reverse(Direction.WEST);
            prev_x = dir_x;
            dir_x = Direction.WEST;
        }
        else {
            check_Reverse(Direction.EAST);
            prev_x = dir_x;
            dir_x = Direction.EAST;
        }
        if(head_point.getY()> fruitpos.getY()) {
            check_Reverse(Direction.SOUTH);
            prev_y = dir_y;
            dir_y = Direction.SOUTH;
        }
        else {
            check_Reverse(Direction.NORTH);
            prev_y = dir_y;
            dir_y = Direction.NORTH;
        }
        fruit_point = fruitpos;
        calculate_Danger_Box();
        is_Danger(fruitpos);
        switch_set = true;
        System.out.println();
        System.out.println("-----------------------------------------");
        System.out.println("dir_x = " + dir_x);
        System.out.println("dir_y = " + dir_y);
        System.out.println("head_point = " + head_point.getX());
        System.out.println("max_x = " + max_x);
        System.out.println("head_point = " + head_point.getX());
        System.out.println("min_x = " + min_x);
        System.out.println("max_y = " + max_y);
        System.out.println("head_point = " + head_point.getY());
        System.out.println("min_y = " + min_y);
        System.out.println("move_danger = " + move_danger);
        System.out.println("-------------------------------------------");
    }

    public void move_Snake_Auto(){
        if(move_danger){
            determine_escape_dir();
            re_Calculate_Danger();
        }
        else{
            if(switch_set){
                head_point = new Point2D.Float(auto_snake.getHead().getPositionX(),auto_snake.getHead().getPositionY());
                //switch_set이 true -> x를 setting
                boolean set_x = (Math.abs(head_point.getX()-fruit_point.getX())<1.0);
                if(set_x){
                    switch_set = false;
                    return;
                }

                if(auto_snake.change_Direction(dir_x)==false)auto_snake.change_Direction(prev_y);
            }
            else{
                boolean set_y = (Math.abs(head_point.getY()-fruit_point.getY())<1.0);
                if(set_y){
                    switch_set = true;
                    return;
                }
                if(auto_snake.change_Direction(dir_y)==false) auto_snake.change_Direction(prev_x);
            }
        }
    }

    private void determine_escape_dir() {

        Direction determined ;
        Direction cand_1 = prev_y;
        Direction cand_2 = prev_x;
        float remain_x;
        float remain_y;
        head_point.setLocation(head.getPositionX(),head.getPositionY());
        remain_x = cand_2 == Direction.WEST ? Math.abs(4- head.getPositionX()) : Math.abs(76- head.getPositionX());
        remain_y = cand_1 == Direction.NORTH ? Math.abs(-4- head.getPositionY()) : Math.abs(-76- head.getPositionY());
        if(remain_x>=remain_y) {
            determined = cand_2;
            auto_snake.change_Direction(cand_2);
            switch_set = false;
            move_danger = false;
        }
        else{
            determined = cand_1;
            auto_snake.change_Direction(cand_1);
            switch_set = true;
            move_danger = false;
        }
    }

    private void check_Reverse(Direction dir){
        switch (dir){
            case NORTH -> {
                flag_y = false;
                if(dir_y == Direction.SOUTH) flag_y = true; break;
            }
            case SOUTH -> {
                flag_y = false;
                if(dir_y == Direction.NORTH) flag_y = true; break;
            }
            case EAST -> {
                flag_x = false;
                if(dir_x == Direction.WEST) flag_x = true; break;
            }
            case WEST -> {
                flag_x = false;
                if(dir_x == Direction.EAST) flag_x = true; break;
            }
        }
    }

    private void re_Calculate_Danger() {
        calculate_Danger_Box();
    }

    private boolean acrossing_Danger_x(Direction will_go){
        //가로지르는 중에 danger의 위험성 check
        head_point.setLocation(head.getPositionX(),head.getPositionY());
        boolean check_y = false;
        if(head_point.getY()>min_y && head_point.getY()<max_y) check_y = true;
        if(check_y == false) return false;
        switch (will_go){
            case EAST -> {
                if(head_point.getX()<max_x) return true;
            }
            case WEST -> {
                if(head_point.getX()>min_x) return true;
            }
        }
        return false;
    }


}
