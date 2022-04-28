package src.domain;

import org.junit.jupiter.api.Test;
import src.controller.*;

class tester {

    @Test
    public void gamestart() throws InterruptedException {
        Snake snake = new Snake();
        GameBoard board = new GameBoard(snake);
       MainController tester = new MainController();
       tester.init(board);
       Thread testth = new Thread(tester);
       KeyController controller = new KeyController(board);
        testth.start();
        testth.join();
    }

}