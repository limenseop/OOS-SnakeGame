package src.domain;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

abstract public class AutoSnake extends Snake {
    private double minDistancefromHead;
    public AutoSnake() {
        super();
        minDistancefromHead = Double.MAX_VALUE;
    }
    public Direction changeDirection(Snake otherSnake, float boardWidthSize, float boardHeightSize, float checkSize, double DistancetoCheck) {
        //1순위 장애물 회피
        Stream<snakeBody> checkObstacles = Stream.concat(otherSnake.getBody().stream(), getBody().stream().skip(1));
        snakeBody closedBody = getClosedObstacle(getOtherSnakeInfo(checkObstacles.toList(), checkSize), DistancetoCheck);
        double distancefromBoundary = getDistancefromBoundary(boardWidthSize, boardHeightSize);

        if (distancefromBoundary < DistancetoCheck) {
            if (minDistancefromHead < distancefromBoundary) {
                if (closedBody != null)
                    return getDirectionbyObstacle(closedBody.getPositionX(), closedBody.getPositionY(), boardWidthSize, boardHeightSize);
                return getDirectionbyBoundary(boardWidthSize, boardHeightSize, checkSize);
            }
            return getDirectionbyBoundary(boardWidthSize, boardHeightSize, checkSize);
        }
        if (closedBody != null) {
            return getDirectionbyObstacle(closedBody.getPositionX(), closedBody.getPositionY(), boardWidthSize, boardHeightSize);
        }

        //2순위 사과로 이동
        return getCurDirection();
    }

    abstract Vector3f getClosedApple(Vector3f[] apples);
    abstract Direction getDirectionbyApple(Vector3f closedapple);
    List<snakeBody> getOtherSnakeInfo(List<snakeBody> checkBodies, float checkSize) throws NullPointerException {
        List<snakeBody> fliteredBodies = new ArrayList<>();
        for (snakeBody body : checkBodies) {
            if (checkXaxle(body.getPositionX(), checkSize) || checkYaxle(body.getPositionY(), checkSize))
                fliteredBodies.add(body);
        }
        return fliteredBodies;
    }
    private Direction getDirectionbyObstacle(float xPosinObstacle, float yPosinObstacle, float boardWidth, float boardHeight) {
        float distanceX = xPosinObstacle - getHead().getPositionX();
        float distanceY = yPosinObstacle - getHead().getPositionY();

        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            if (distanceX >= 0) {
                if (getCurDirection() == Direction.EAST) {
                    return getDirectionbyCenter(boardWidth, boardHeight, Direction.EAST);
                } else
                    return Direction.WEST;
            } else {
                if (getCurDirection() == Direction.WEST) {
                    return getDirectionbyCenter(boardWidth, boardHeight, Direction.WEST);
                } else
                    return Direction.EAST;

            }
        } else {
            if (distanceY >= 0) {
                if (getCurDirection() == Direction.NORTH) {
                    return getDirectionbyCenter(boardWidth, boardHeight, Direction.NORTH);
                } else
                    return Direction.SOUTH;
            } else {
                if (getCurDirection() == Direction.SOUTH) {
                    return getDirectionbyCenter(boardWidth, boardHeight, Direction.SOUTH);
                } else
                    return Direction.NORTH;
            }
        }
    }
    private Direction getDirectionbyBoundary(float boardWidth, float boardHeight, float checkSize) {
        float headXpos = getHead().getPositionX();
        float headYpos = getHead().getPositionY();
        if (headXpos <= checkSize) {
            if (getCurDirection() == Direction.EAST)
                return getDirectionbyCenter(boardWidth, boardHeight, Direction.EAST);
            else
                return Direction.WEST;
        } else if (headXpos >= boardWidth - checkSize) {
            if (getCurDirection() == Direction.WEST)
                return getDirectionbyCenter(boardWidth, boardHeight, Direction.WEST);
            else
                return Direction.EAST;
        } else if (headYpos >= -checkSize) {
            if (getCurDirection() == Direction.NORTH)
                return getDirectionbyCenter(boardWidth, boardHeight, Direction.NORTH);
            else
                return Direction.SOUTH;
        } else if (headYpos <= -boardHeight + checkSize) {
            if (getCurDirection() == Direction.SOUTH)
                return getDirectionbyCenter(boardWidth, boardHeight, Direction.SOUTH);
            else
                return Direction.NORTH;
        } else
            return getCurDirection();
    }
    private Direction getDirectionbyCenter(float boardWidth, float boardHeight, Direction dir) {
        switch(dir) {
            case NORTH:
            case SOUTH:
                if (getHead().getPositionX() >= boardWidth/2)
                    return Direction.WEST;
                else
                    return Direction.EAST;
            case EAST:
            case WEST:
                if (getHead().getPositionY() >= -boardHeight/2)
                    return Direction.SOUTH;
                else
                    return Direction.NORTH;
        }
        return getCurDirection();
    }
    private snakeBody getClosedObstacle(List<snakeBody> bodies, double DistancetoCheck) {
        snakeBody closedBody = null;
        minDistancefromHead = Double.MAX_VALUE;
        try {
            for (snakeBody body : bodies) {
                double curDistance = getDistancefromHead(body);
                if (curDistance <= DistancetoCheck && curDistance < minDistancefromHead) {
                    minDistancefromHead = curDistance;
                    closedBody = body;
                }
            }
            return closedBody;
        } catch (NullPointerException e) {
            System.out.println("There are no snakes on X,Y axles");
            return null;
        }
    }
    private double getDistancefromHead(snakeBody body) {
        double xdis = Math.pow((getHead().getPositionX() - body.getPositionX()), 2);
        double ydis = Math.pow((getHead().getPositionY() - body.getPositionY()), 2);
        return Math.sqrt(xdis+ydis);
    }
    private double getDistancefromBoundary(float boardWidth, float boardHeight) {
        float xLeft = getHead().getPositionX();
        float xRight = Math.abs(boardWidth - getHead().getPositionX());
        float yUp = Math.abs(getHead().getPositionY());
        float yDown = Math.abs(-boardHeight - getHead().getPositionY());
        return Math.min(Math.min(xLeft, xRight), Math.min(yUp, yDown));
    }
    private boolean checkXaxle(float x, float checkSize) {
        if (x+checkSize >= getHead().getPositionX() && x-checkSize <= getHead().getPositionX())
            return true;
        else
            return false;
    }
    private boolean checkYaxle(float y, float checkSize) {
        if (y+checkSize >= getHead().getPositionY() && y-checkSize <= getHead().getPositionY())
            return true;
        else
            return false;
    }
    private Direction getCurDirection() {
        return getHead().getDirection();
    }
}