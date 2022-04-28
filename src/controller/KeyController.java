package src.controller;

import src.domain.GameBoard;
import src.domain.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class KeyController extends JFrame {

    private GameBoard gameboard;
    private boolean paused_Option = false;

    public KeyController(GameBoard gameboard) throws HeadlessException {
        this.gameboard = gameboard;
        setTitle("hello");
        setSize(10,10);
        setVisible(true);

        Panel p = new Panel();
        List l = new List(5); // List에 focus를 주기위함.
        p.add(l);
        add(p);

        l.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()=='r'){
                    gameboard.switch_Pause();
                    paused_Option = (!paused_Option);
                    System.out.println("paused_Option = " + paused_Option);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if((!paused_Option)){
                    switch (e.getKeyCode()) {
                        case 37: {
                                gameboard.change_Direction_Snake(Direction.WEST);
                            break;
                        }
                        case 38: {
                                gameboard.change_Direction_Snake(Direction.NORTH);
                            break;
                        }
                        case 39: {
                                gameboard.change_Direction_Snake(Direction.EAST);
                            break;
                        }
                        case 40: {
                                gameboard.change_Direction_Snake(Direction.SOUTH);
                            break;
                        }
                    }
                }
                else{
                    switch(e.getKeyChar()){
                        case 's' : {
                            try {
                                save_This_Game();
                                System.out.println("game saved!");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        }
                        case 'l' : {
                            break;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void save_This_Game() throws IOException {
        FileOutputStream fos=new FileOutputStream("user.acc");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(gameboard);
    }


    public GameBoard getGameboard() {
        return gameboard;
    }
}
