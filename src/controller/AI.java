package src.controller;

import src.domain.Direction;
import src.domain.GameBoard;
import src.domain.Snake;
import src.domain.snakeBody;

public class AI {
	private GameBoard gameboard;
	
	public AI() {
		
	}
	public void logic () {
		float fruit_X = gameboard.getFruitPositionX();
		float fruit_Y = gameboard.getFruitPositionY();
		float headX = Snake.getHead().getPositionX();
		float headY = Snake.getHead().getPositionY();
		Direction direction = snakeBody.getDirection();
		
		
		if (direction == Direction.WEST) {
			if (fruit_X == headX) {
				if (fruit_Y > headY)

					gameboard.change_Direction_Snake(Direction.NORTH);

				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.SOUTH);
			}
			if (fruit_Y == headY) {
				if (fruit_X < headX)
					gameboard.change_Direction_Snake(Direction.NORTH);
			}
			if (fruit_X > headX) {
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.NORTH);
				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.SOUTH);
			}
			
		}
		if (direction == Direction.EAST) {
			if (fruit_X == headX) { // Fruit와 SnakeHead의 x좌표 값이 같지만 y좌표가 다른 경우
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.NORTH);
				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.SOUTH);
			}
			if (fruit_Y == headY) {
				if (fruit_X > headX)
					gameboard.change_Direction_Snake(Direction.SOUTH);

			}

			if (fruit_X < headX) {
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.NORTH);
				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.SOUTH);
			}
			
		}
		if (direction == Direction.NORTH) {
			if (fruit_X == headX) {

				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.EAST);
			}
			if (fruit_Y == headY) {
				if (fruit_X > headX)
					gameboard.change_Direction_Snake(Direction.EAST);
				if (fruit_X < headX)
					gameboard.change_Direction_Snake(Direction.WEST);
			}
			if (fruit_X > headX) {

				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.EAST);
			}
			if (fruit_X < headX) {

				if (fruit_Y < headY)
					gameboard.change_Direction_Snake(Direction.WEST);
			}
			
		}
		if (direction == Direction.SOUTH) {
			if (fruit_X == headX) {
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.WEST);

			}
			if (fruit_Y == headY) {
				if (fruit_X > headX)
					gameboard.change_Direction_Snake(Direction.EAST);
				if (fruit_X < headX)
					gameboard.change_Direction_Snake(Direction.WEST);
			}
			if (fruit_X > headX) {
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.EAST);

			}
			if (fruit_X < headX) {
				if (fruit_Y > headY)
					gameboard.change_Direction_Snake(Direction.WEST);

			}
			
		}
	}
}
