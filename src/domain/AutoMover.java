package src.domain;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Stream;

public class AutoMover {
    private final float DETECTING_RANGE;
    private final float ERROR_RANGE;
    private final float BOARD_WIDTH;
    private final float BOARD_HEIGHT;
    public AutoMover(float boardWidth, float boardHeight, float errorRange, float detectingRange) {
        ERROR_RANGE = errorRange;
        DETECTING_RANGE = detectingRange;
        BOARD_HEIGHT = boardHeight;
        BOARD_WIDTH = boardWidth;
    }
    public Direction getNextDirection(Snake ownSnake, Snake otherSnake, List<Point2D> fruits) {
        //1순위 장애물 회피
        //System.out.printf("Head pos: %f, %f\n", ownSnake.getHead().getPositionX(), ownSnake.getHead().getPositionY());
        Stream<snakeBody> snakeBodyStream = Stream.concat(ownSnake.getBody().stream().skip(3), otherSnake.getBody().stream());
        snakeBody filteredbody = getFilteredBody(ownSnake, snakeBodyStream.toList());

        if ((filteredbody != null) && (getDistancetoBoundary(ownSnake) < DETECTING_RANGE)) {
            if (getDistancefromBody(ownSnake, filteredbody) < getDistancetoBoundary(ownSnake)) {
                System.out.print(1 + ": Detecting an obstacle. Changing the direction.\n");
                return getDirectionbyBody(ownSnake, filteredbody);
            } else {
                System.out.print(2 + ": Detecting the boundary get closed. Changing the direction.\n");
                return getDirectionbyCenter(ownSnake);
            }
        } else if (filteredbody != null) {
            System.out.print(3 + ": Detecting an obstacle. Changing the direction.\n");
            return getDirectionbyBody(ownSnake, filteredbody);
        } else if (getDistancetoBoundary(ownSnake) < DETECTING_RANGE) {
            System.out.print(4 + ": Detecting the boundary get closed. Changing the direction.\n");
            return getDirectionbyCenter(ownSnake);
        }
        return getDirectionbyApple(ownSnake, getClosedApple(ownSnake, fruits));
    }
    private Point2D getClosedApple(Snake ownSanke, List<Point2D> apples){
        double mindistance = Double.MAX_VALUE;
        Point2D closedapple = null;
        Point2D head = new Point2D.Float(ownSanke.getHead().getPositionX(), ownSanke.getHead().getPositionY());
        for (Point2D apple : apples) {
            double distancetoHead = head.distance(apple);
            if (distancetoHead < mindistance) {
                closedapple = apple;
                mindistance = distancetoHead;
            }
        }
        return closedapple;
    }
    private Direction getDirectionbyApple(Snake ownsnake, Point2D closedapple) {
        double appleXpos = closedapple.getX();
        double appleYpos = closedapple.getY();
        double headXpos = ownsnake.getHead().getPositionX();
        double headYpos = ownsnake.getHead().getPositionY();

        if (Math.abs(headXpos - appleXpos) >= 0.5) {
            if (headXpos < appleXpos) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        } else {
            if (headYpos < appleYpos) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        }
    }
    private snakeBody getFilteredBody(Snake ownsnake, List<snakeBody> checkBodies) {
        snakeBody filteredBody = null;
        double mindistance = Double.MAX_VALUE;
        try {
            for (snakeBody body : checkBodies) {
                if ((getDistancefromBody(ownsnake, body) <= DETECTING_RANGE)
                        && (ownsnake.getHead().getDirection() != body.getDirection())
                        && (detectAxlebyDirection(ownsnake, body))) {
                    if (mindistance > getDistancefromBody(ownsnake, body)) {
                        mindistance = getDistancefromBody(ownsnake, body);
                        filteredBody = body;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Can't find bodies");
            return null;
        }
        return filteredBody;
    }
    private Direction getDirectionbyCenter(Snake ownsnake) {
        switch(ownsnake.getHead().getDirection()) {
            case NORTH:
            case SOUTH:
                if (ownsnake.getHead().getPositionY() >= BOARD_HEIGHT/2)
                    return Direction.WEST;
                else
                    return Direction.EAST;
            case EAST:
            case WEST:
                if (ownsnake.getHead().getPositionX() >= BOARD_WIDTH/2)
                    return Direction.NORTH;
                else
                    return Direction.SOUTH;
        }
        return ownsnake.getDirection();
    }
    private Direction getDirectionbyBody(Snake ownsnake, snakeBody body) {
        switch(ownsnake.getHead().getDirection()) {
            case SOUTH, NORTH -> {
                if (body.getDirection() == Direction.WEST)
                    return Direction.EAST;
                else if (body.getDirection() == Direction.EAST)
                    return Direction.WEST;
            }
            case WEST, EAST -> {
                if (body.getDirection() == Direction.NORTH)
                    return Direction.SOUTH;
                else if (body.getDirection() == Direction.SOUTH)
                    return Direction.NORTH;
            }
        }
        return ownsnake.getDirection();
    }
    private boolean detectAxlebyDirection(Snake ownsnake, snakeBody body) {
        float xLeftRange = ownsnake.getHead().getPositionX() - ERROR_RANGE;
        float xRightRange = ownsnake.getHead().getPositionX() + ERROR_RANGE;
        float yUpRange = ownsnake.getHead().getPositionY() + ERROR_RANGE;
        float yDownRange = ownsnake.getHead().getPositionY() - ERROR_RANGE;

        switch (ownsnake.getHead().getDirection()) {
            case NORTH -> {
                if (body.getPositionY() > ownsnake.getHead().getPositionY()) {
                    return body.getPositionX() >= xLeftRange && body.getPositionX() <= xRightRange;
                }
                return false;
            }
            case EAST -> {
                if (body.getPositionX() > ownsnake.getHead().getPositionX()) {
                    return body.getPositionY() <= yUpRange && body.getPositionY() >= yDownRange;
                }
                return false;
            }
            case WEST -> {
                if (body.getPositionX() < ownsnake.getHead().getPositionX()) {
                    return body.getPositionY() <= yUpRange && body.getPositionY() >= yDownRange;
                }
                return false;
            }
            case SOUTH -> {
                if (body.getPositionY() < ownsnake.getHead().getPositionY()) {
                    return body.getPositionX() >= xLeftRange && body.getPositionX() <= xRightRange;
                }
                return false;
            }
        }
        return false;
    }
    private double getDistancefromPos(Snake ownsnake, float x, float y) {
        double xdis = Math.pow((ownsnake.getHead().getPositionX() - x), 2);
        double ydis = Math.pow((ownsnake.getHead().getPositionY() - y), 2);
        return Math.sqrt(xdis+ydis);
    }
    private double getDistancefromBody(Snake ownsnake, snakeBody body) {
        double xdis = Math.pow((body.getPositionX() - ownsnake.getHead().getPositionX()), 2);
        double ydis = Math.pow((body.getPositionY() - ownsnake.getHead().getPositionY()), 2);
        return Math.sqrt(xdis+ydis);
    }
    private float getDistancetoBoundary (Snake ownsnake) {
        switch(ownsnake.getHead().getDirection()) {
            case NORTH -> {
                return Math.abs(ownsnake.getHead().getPositionY());
            }
            case SOUTH -> {
                return Math.abs(ownsnake.getHead().getPositionY() - BOARD_HEIGHT);
            }
            case EAST -> {
                return Math.abs(ownsnake.getHead().getPositionX() - BOARD_WIDTH);
            }
            case WEST -> {
                return Math.abs(ownsnake.getHead().getPositionX());
            }
        }
        return Float.MAX_VALUE;
    }
}