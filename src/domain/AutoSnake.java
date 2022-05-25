package src.domain;

import org.joml.Vector3f;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AutoSnake {
    private double minDistancefromHead;
    public AutoSnake() {
        minDistancefromHead = Double.MAX_VALUE;
    }
    public Direction changeDirection(Snake ownSnake, Snake otherSnake, List<Point2D> apples, float boardWidthSize, float boardHeightSize, float checkSize, double DistancetoCheck) {
        //1순위 장애물 회피
        Stream<snakeBody> checkObstacles = Stream.concat(otherSnake.getBody().stream(), ownSnake.getBody().stream().skip(3));
        snakeBody closedBody = getClosedObstacle(ownSnake,getOtherSnakeInfo(ownSnake,checkObstacles.toList(), checkSize), DistancetoCheck);
        double distancefromBoundary = getDistancefromBoundary(ownSnake,boardWidthSize, boardHeightSize);

        if (distancefromBoundary < DistancetoCheck) {
            if (minDistancefromHead < distancefromBoundary) {
                if (closedBody != null)
                    return getDirectionbyObstacle(ownSnake,closedBody.getPositionX(), closedBody.getPositionY(), boardWidthSize, boardHeightSize);
                return getDirectionbyBoundary(ownSnake,boardWidthSize, boardHeightSize, checkSize);
            }
            return getDirectionbyBoundary(ownSnake,boardWidthSize, boardHeightSize, checkSize);
        }
        if (closedBody != null) {
            return getDirectionbyObstacle(ownSnake,closedBody.getPositionX(), closedBody.getPositionY(), boardWidthSize, boardHeightSize);
        }

        //2순위 사과로 이동
        return getDirectionbyApple(ownSnake, getClosedApple(ownSnake, apples));
    }
    private Point2D getClosedApple(Snake ownSanke, List<Point2D> apples){
        double mindistance = Double.MAX_VALUE;
        Point2D closedapple = null;
        Point2D head = new Point2D.Float(ownSanke.getHead().getPositionX(), ownSanke.getHead().getPositionY());
        for (int i = 0; i < apples.size(); i++) {
            double distancetoHead = head.distance(apples.get(i)); 
            if (distancetoHead < mindistance) {
                closedapple = apples.get(i);
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
    List<snakeBody> getOtherSnakeInfo(Snake ownsnake, List<snakeBody> checkBodies, float checkSize) throws NullPointerException {
        List<snakeBody> fliteredBodies = new ArrayList<>();
        for (snakeBody body : checkBodies) {
            if (checkXaxle(ownsnake, body.getPositionX(), checkSize) || checkYaxle(ownsnake, body.getPositionY(), checkSize))
                fliteredBodies.add(body);
        }
        return fliteredBodies;
    }
    private Direction getDirectionbyObstacle(Snake ownsnake, float xPosinObstacle, float yPosinObstacle, float boardWidth, float boardHeight) {
        float distanceX = xPosinObstacle - ownsnake.getHead().getPositionX();
        float distanceY = yPosinObstacle - ownsnake.getHead().getPositionY();

        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            if (distanceX >= 0) {
                if (getCurDirection(ownsnake) == Direction.EAST) {
                    return getDirectionbyCenter(ownsnake,boardWidth, boardHeight, Direction.EAST);
                } else
                    return Direction.WEST;
            } else {
                if (getCurDirection(ownsnake) == Direction.WEST) {
                    return getDirectionbyCenter(ownsnake, boardWidth, boardHeight, Direction.WEST);
                } else
                    return Direction.EAST;

            }
        } else {
            if (distanceY >= 0) {
                if (getCurDirection(ownsnake) == Direction.NORTH) {
                    return getDirectionbyCenter(ownsnake,boardWidth, boardHeight, Direction.NORTH);
                } else
                    return Direction.SOUTH;
            } else {
                if (getCurDirection(ownsnake) == Direction.SOUTH) {
                    return getDirectionbyCenter(ownsnake,boardWidth, boardHeight, Direction.SOUTH);
                } else
                    return Direction.NORTH;
            }
        }
    }
    private Direction getDirectionbyBoundary(Snake ownsnake, float boardWidth, float boardHeight, float checkSize) {
        float headXpos = ownsnake.getHead().getPositionX();
        float headYpos = ownsnake.getHead().getPositionY();
        if (headXpos <= checkSize) {
            if (getCurDirection(ownsnake) == Direction.EAST)
                return getDirectionbyCenter(ownsnake,boardWidth, boardHeight, Direction.EAST);
            else
                return Direction.WEST;
        } else if (headXpos >= boardWidth - checkSize) {
            if (getCurDirection(ownsnake) == Direction.WEST)
                return getDirectionbyCenter(ownsnake,boardWidth, boardHeight, Direction.WEST);
            else
                return Direction.EAST;
        } else if (headYpos >= -checkSize) {
            if (getCurDirection(ownsnake) == Direction.NORTH)
                return getDirectionbyCenter(ownsnake, boardWidth, boardHeight, Direction.NORTH);
            else
                return Direction.SOUTH;
        } else if (headYpos <= -boardHeight + checkSize) {
            if (getCurDirection(ownsnake) == Direction.SOUTH)
                return getDirectionbyCenter(ownsnake, boardWidth, boardHeight, Direction.SOUTH);
            else
                return Direction.NORTH;
        } else
            return getCurDirection(ownsnake);
    }
    private Direction getDirectionbyCenter(Snake ownsnake, float boardWidth, float boardHeight, Direction dir) {
        switch(dir) {
            case NORTH:
            case SOUTH:
                if (ownsnake.getHead().getPositionX() >= boardWidth/2)
                    return Direction.WEST;
                else
                    return Direction.EAST;
            case EAST:
            case WEST:
                if (ownsnake.getHead().getPositionY() >= -boardHeight/2)
                    return Direction.SOUTH;
                else
                    return Direction.NORTH;
        }
        return getCurDirection(ownsnake);
    }
    private snakeBody getClosedObstacle(Snake ownsnake, List<snakeBody> bodies, double DistancetoCheck) {
        snakeBody closedBody = null;
        minDistancefromHead = Double.MAX_VALUE;
        try {
            for (snakeBody body : bodies) {
                double curDistance = getDistancefromHead(ownsnake, body);
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
    private double getDistancefromHead(Snake ownsnake, float x, float y) {
        double xdis = Math.pow((ownsnake.getHead().getPositionX() - x), 2);
        double ydis = Math.pow((ownsnake.getHead().getPositionY() - y), 2);
        return Math.sqrt(xdis+ydis);
    }
    private double getDistancefromHead(Snake ownsnake, snakeBody body) {
        double xdis = Math.pow((ownsnake.getHead().getPositionX() - body.getPositionX()), 2);
        double ydis = Math.pow((ownsnake.getHead().getPositionY() - body.getPositionY()), 2);
        return Math.sqrt(xdis+ydis);
    }
    private double getDistancefromBoundary(Snake ownsnake, float boardWidth, float boardHeight) {
        float xLeft = ownsnake.getHead().getPositionX();
        float xRight = Math.abs(boardWidth - ownsnake.getHead().getPositionX());
        float yUp = Math.abs(ownsnake.getHead().getPositionY());
        float yDown = Math.abs(-boardHeight - ownsnake.getHead().getPositionY());
        return Math.min(Math.min(xLeft, xRight), Math.min(yUp, yDown));
    }
    private boolean checkXaxle(Snake ownsnake, float x, float checkSize) {
        if (x+checkSize >= ownsnake.getHead().getPositionX() && x-checkSize <= ownsnake.getHead().getPositionX())
            return true;
        else
            return false;
    }
    private boolean checkYaxle(Snake ownsnake, float y, float checkSize) {
        if (y+checkSize >= ownsnake.getHead().getPositionY() && y-checkSize <= ownsnake.getHead().getPositionY())
            return true;
        else
            return false;
    }
    private Direction getCurDirection(Snake snake) {
        return snake.getHead().getDirection();
    }
}