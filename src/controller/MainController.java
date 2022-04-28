package src.controller;

import src.domain.GameBoard;
import src.domain.Direction;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

import static java.lang.Thread.sleep;

public class MainController extends JFrame implements Runnable, ActionListener ,KeyListener {

    private GameBoard gameboard;
    private KeyController checker;
    int counter = 0;

    public void init(GameBoard board){
        gameboard = board;
    }

    /**
     * https://www.youtube.com/watch?v=bI6e6qjJ8JQ
     */
    public void run(){
        while((gameboard.gameRunning())) {
            if(gameboard.isPaused()){
                //gameboard.gamePause();
            }
            gameboard.move_Snake();
            gameboard.check_Fruit_Overlap();
            gameboard.check_Game_Terminated();
            try {
                sleep(300);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter = counter + 1;
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

    public GameBoard load_Game() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("user.acc");
        ObjectInputStream ois=new ObjectInputStream(fis);
        gameboard=(GameBoard)ois.readObject();
        return gameboard;
    }

    private void game_Terminate(){
        System.out.println("***********************************");
        gameboard.showRanking();
        System.out.println("Press 'R' to replay");
        System.out.println("***********************************");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyCode()==37){
            gameboard.change_Direction_Snake(Direction.WEST);
        }
        if(e.getKeyCode()==38){
            gameboard.change_Direction_Snake(Direction.NORTH);
        }
        if(e.getKeyCode()==39){
            gameboard.change_Direction_Snake(Direction.EAST);
        }
        if(e.getKeyCode()==40){
            gameboard.change_Direction_Snake(Direction.SOUTH);
        }
        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return;
    }

    public GameBoard getGameboard() {
        return gameboard;
    }
}

