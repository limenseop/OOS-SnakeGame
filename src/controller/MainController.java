package src.controller;

import src.domain.Board;
import src.domain.Direction;

import java.io.*;

import static java.lang.Thread.sleep;

public class MainController implements Runnable{

    private Board gameboard;
    int count = 0;

    public void init(Board board){
        gameboard = board;
    }

    public void run(){
        while((!gameboard.gameTermination())) {
            gameboard.move_Snake();
            gameboard.brief();
            try {
                sleep(300);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = count + 1;
            System.out.println("count = " + count);
            if(!gameboard.getRunning()){
                gameboard.gamePause();
            }
        }
        //game terminate!
        gameboard.recordRanking();
        game_Terminate();
    }

    public void save_This_Game() throws IOException {
        FileOutputStream fos=new FileOutputStream("user.acc");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(gameboard);
    }

    public Board load_Game() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("user.acc");
        ObjectInputStream ois=new ObjectInputStream(fis);
        gameboard=(Board)ois.readObject();
        return gameboard;
    }

    private void game_Terminate(){
        System.out.println("***********************************");
        gameboard.showRanking();
        System.out.println("Press 'R' to replay");
        System.out.println("***********************************");
    }

}

