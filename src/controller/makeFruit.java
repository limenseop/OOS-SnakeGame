package src.controller;

import src.domain.Board;
import src.domain.Direction;

public class makeFruit extends Thread{

    private Board gameboard;

    public void init(Board board){
        gameboard = board;
    }

    public void run(){
        while(true) {
            System.out.println("fruitMaker.run");
            gameboard.createFruit();
            if(gameboard.gameTermination()) break;
            try {
                sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
