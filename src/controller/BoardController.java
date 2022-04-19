package src.controller;

import src.domain.Board;

import static java.lang.Thread.sleep;

public class BoardController implements Runnable{

    private Board gameboard;

    public void init(Board board){
        gameboard = board;
    }

    public void run(){
        while(true){
            gameboard.move_Snake();
            gameboard.brief();
            if(gameboard.gameTermination()) {
                System.out.println("BoardController.terminate game!");
                System.out.println("score : " + gameboard.getScore());
                //여기서 종료 옵션 내놓으면 될듯
                break;
            }
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

