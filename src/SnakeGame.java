package src;

import src.controller.LWJGL_Controller;
import src.domain.GameBoard;
import src.domain.Snake;

public class SnakeGame {

    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard(new Snake());
        LWJGL_Controller controller = new LWJGL_Controller(gameBoard);
        controller.run();
    }

}
