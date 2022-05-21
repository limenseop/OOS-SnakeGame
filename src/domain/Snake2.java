package src.domain;

import src.models.Camera;
import src.models.Shader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake2 implements Serializable {

	private snakeBody head;

	public Direction getDirection() {
		return direction;
	}

	private List<snakeBody> body;

	private Direction direction;

	public snakeBody getHead() {
		return head;
	}

	public List<snakeBody> getBody() {
		return body;
	}

	public Snake2() {
		body = new ArrayList<>();
		body.add(new snakeBody(79, -79, Direction.WEST));
		body.add(new snakeBody(80, -79, Direction.WEST));
		body.add(new snakeBody(81, -79, Direction.WEST));
		body.add(new snakeBody(79, -79, Direction.WEST));
		body.add(new snakeBody(78, -79, Direction.WEST));
		body.add(new snakeBody(77, -79, Direction.WEST));
		body.add(new snakeBody(76, -79, Direction.WEST));
		body.add(new snakeBody(75, -79, Direction.WEST));
		body.add(new snakeBody(74, -79, Direction.WEST));
		direction = Direction.WEST;
		head = body.get(0);
	}

	public boolean grow() {
		snakeBody tail = body.get(body.size() - 1);

		float x = tail.getPositionX();
		float y = tail.getPositionY();
		float new_X = 0;
		float new_Y = 0;

		switch (direction) {
		case NORTH: {
			new_X = x;
			new_Y = y - 0.4f;
			break;
		}
		case SOUTH: {
			new_X = x;
			new_Y = y + 0.4f;
			break;
		}
		case EAST: {
			new_X = x - 0.4f;
			new_Y = y;
			break;
		}
		case WEST: {
			new_X = x + 0.4f;
			new_Y = y;
			break;
		}
		}
		snakeBody newBody = new snakeBody(new_X, new_Y, direction);
		body.add(newBody);
		return true;
	}

	public boolean change_Direction(Direction direction) {
		if (this.direction == Direction.EAST && direction == Direction.WEST
				|| this.direction == Direction.WEST && direction == Direction.EAST
				|| this.direction == Direction.NORTH && direction == Direction.SOUTH
				|| this.direction == Direction.SOUTH && direction == Direction.NORTH)
			return false;
		this.direction = direction;
		head.changeDirection(direction);
		return true;
	}

	public void move() {
		float prev_savedX = 0;
		float prev_savedY = 0;
		float savedX = 0;
		float savedY = 0;
		Direction savedDirection = Direction.NORTH;
		List<snakeBody> prev_body = body;
		for (int i = 0; i < body.toArray().length; i++) {
			prev_savedX = body.get(i).getPositionX();
			prev_savedY = body.get(i).getPositionY();
			savedDirection = body.get(i).getDirection();
			if (i == 0) {
				body.get(i).move();
			} else {
				body.get(i).movePosition(savedX, savedY, savedDirection);
			}
			savedX = prev_savedX;
			savedY = prev_savedY;
		}
	}

	public boolean check_If_collapse() {
		Point headPoint = new Point((int) head.getPositionX(), (int) head.getPositionY());
		for (snakeBody snakeBody : body) {
			if (snakeBody.equals(head))
				continue;
			if (head.getPositionX() == snakeBody.getPositionX() && head.getPositionY() == snakeBody.getPositionY()) {
				System.out.println("collapse occurs!!");
				return true;
			}
		}
		return false;
	}

	public boolean check_If_Overlap(float x, float y) {
		Point2D.Float checker = new Point2D.Float(x, y);
		for (snakeBody snakeBody : body) {
			Point2D.Float body = new Point2D.Float(snakeBody.getPositionX(), snakeBody.getPositionY());
			if (checker.distance(body) < 1)
				return true;
		}
		return false;
	}

	public void re_Init() {
		body.clear();
		System.out.println("Snake.setInit");
		body.add(new snakeBody(79, -79, Direction.WEST));
		body.add(new snakeBody(80, -79, Direction.WEST));
		body.add(new snakeBody(81, -79, Direction.WEST));
		body.add(new snakeBody(79, -79, Direction.WEST));
		body.add(new snakeBody(78, -79, Direction.WEST));
		body.add(new snakeBody(77, -79, Direction.WEST));
		body.add(new snakeBody(76, -79, Direction.WEST));
		body.add(new snakeBody(75, -79, Direction.WEST));
		body.add(new snakeBody(74, -79, Direction.WEST));
		head = body.get(0);
		System.out.println("this.head = " + this.head);
	}

	public void setSnake(Snake2 snakes) {
		body.clear();
		for (snakeBody snakeBody : snakes.body) {
			this.body.add(snakeBody);
		}
		head = this.body.get(0);
	}
}
