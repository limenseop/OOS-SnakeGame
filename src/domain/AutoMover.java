package src.domain;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Stream;

public class AutoMover {
    private final float DETECTING_RANGE = 2;
    public AutoMover() {

    }
    public Direction changeDirection(Snake ownSnake, Snake otherSnake, List<Point2D> apples, float boardWidth, float boardHeight, float distancetoCheck) {
        //1순위 장애물 회피
        //System.out.printf("Head pos: %f, %f\n", ownSnake.getHead().getPositionX(), ownSnake.getHead().getPositionY());
        Stream<snakeBody> snakeBodyStream = Stream.concat(ownSnake.getBody().stream().skip(3), otherSnake.getBody().stream());
        snakeBody filteredbody = getFilteredBody(ownSnake, snakeBodyStream.toList(), distancetoCheck);

        if ((filteredbody != null) && (getDistancetoBoundary(ownSnake, boardWidth, boardHeight) < distancetoCheck)) {
            if (getDistancefromBody(ownSnake, filteredbody) < getDistancetoBoundary(ownSnake, boardWidth, boardHeight)) {
                return getDirectionbyBody(ownSnake, filteredbody);
            } else {
                return getDirectionbyCenter(ownSnake, boardWidth, boardHeight);
            }

        } else if (filteredbody != null) {
            return getDirectionbyBody(ownSnake, filteredbody);
        } else if (getDistancetoBoundary(ownSnake, boardWidth, boardHeight) < distancetoCheck) {
            return getDirectionbyCenter(ownSnake, boardWidth, boardHeight);
        }
        return ownSnake.getHead().getDirection();
        //2순위 사과로 이동
        //return getDirectionbyApple(ownSnake, getClosedApple(ownSnake, apples));
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
        double xdis = closedapple.getX() - ownsnake.getHead().getPositionX();
        double ydis = closedapple.getY() - ownsnake.getHead().getPositionY();
        if (Math.abs(xdis) > Math.abs(ydis)) {
            if (xdis > 0)
                return Direction.WEST;
            else
                return Direction.EAST;
        } else {
            if (ydis > 0)
                return Direction.NORTH;
            else
                return Direction.SOUTH;
        }
    }
    private snakeBody getFilteredBody(Snake ownsnake, List<snakeBody> checkBodies, double distancetoCheck) {
        snakeBody filteredBody = null;
        double mindistance = Double.MAX_VALUE;
        try {
            for (snakeBody body : checkBodies) {
                if ((getDistancefromBody(ownsnake, body) <= distancetoCheck)
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
    private Direction getDirectionbyCenter(Snake ownsnake, float boardWidth, float boardHeight) {
        System.out.println("change direction by Center");
        switch(ownsnake.getHead().getDirection()) {
            case NORTH:
            case SOUTH:
                if (ownsnake.getHead().getPositionY() >= boardHeight/2)
                    return Direction.WEST;
                else
                    return Direction.EAST;
            case EAST:
            case WEST:
                if (ownsnake.getHead().getPositionX() >= boardWidth/2)
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
        System.out.println("change direction by body");
        return ownsnake.getDirection();
    }
    private boolean detectAxlebyDirection(Snake ownsnake, snakeBody body) {
        float xLeftRange = ownsnake.getHead().getPositionX() - DETECTING_RANGE;
        float xRightRange = ownsnake.getHead().getPositionX() + DETECTING_RANGE;
        float yUpRange = ownsnake.getHead().getPositionY() + DETECTING_RANGE;
        float yDownRange = ownsnake.getHead().getPositionY() - DETECTING_RANGE;

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
    private float getDistancetoBoundary (Snake ownsnake, float boardWidth, float boardHeight) {
        switch(ownsnake.getHead().getDirection()) {
            case NORTH -> {
                return Math.abs(ownsnake.getHead().getPositionY());
            }
            case SOUTH -> {
                return Math.abs(ownsnake.getHead().getPositionY() - boardHeight);
            }
            case EAST -> {
                return Math.abs(ownsnake.getHead().getPositionX() - boardWidth);
            }
            case WEST -> {
                return Math.abs(ownsnake.getHead().getPositionX());
            }
        }
        return Float.MAX_VALUE;
    }
}