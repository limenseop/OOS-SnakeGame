package src.controller;

import src.domain.Board;
import src.domain.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController extends JFrame {

    private Board gameboard;
    private boolean paused_Option = false;

    public KeyController(Board gameboard) throws HeadlessException {
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
                switch(e.getKeyCode()){
                    case 37 :{
                        if(!paused_Option)
                        gameboard.change_Direction_Snake(Direction.WEST);
                        break;
                    }
                    case 38 :{
                        if(!paused_Option)
                        gameboard.change_Direction_Snake(Direction.NORTH);
                        break;
                    }
                    case 39 :{
                        if(!paused_Option)
                        gameboard.change_Direction_Snake(Direction.EAST);
                        break;
                    }
                    case 40 :{
                        if(!paused_Option)
                        gameboard.change_Direction_Snake(Direction.SOUTH);
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }


}
