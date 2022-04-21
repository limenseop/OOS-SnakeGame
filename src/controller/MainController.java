package src.controller;

import src.domain.Board;
import src.domain.Direction;

import static java.lang.Thread.sleep;

public class MainController implements Runnable{


    private int counter = 0;
    private Board gameboard;

    public void init(Board board){
        gameboard = board;
    }

    public void run(){

        while((!gameboard.gameTermination())) {
            counter = counter + 1;
            gameboard.move_Snake();
            gameboard.brief();
            try {
                sleep(300);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            changeDirection(counter);
            if(counter == 10){
                gameboard.gamePause();
            }
        }
        //game terminate!
        gameboard.recordRanking();
        game_Terminate();
    }

    private void changeDirection(int counter){
        if(counter % 10 == 3){
            gameboard.change_Direction_Snake(Direction.WEST);
        }
        else if(counter % 10 == 6){
            gameboard.change_Direction_Snake(Direction.NORTH);
        }
    }

    private void game_Terminate(){
        System.out.println("***********************************");
        gameboard.showRanking();
        System.out.println("Press 'R' to replay");
        System.out.println("***********************************");
        gameboard.re_Play();
        gameboard.brief();
        run();
    }
}

