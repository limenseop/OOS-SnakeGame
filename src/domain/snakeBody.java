package src.domain;

public class snakeBody {

    private int positionX;
    private int positionY;
    private Direction direction;

    public snakeBody(int positionX, int positionY, Direction direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction direction){
        this.direction = direction;
    }

    public void move(){
        switch (direction){
            case EAST:{
                positionX = positionX+1;
                break;
            }
            case WEST:{
                positionX = positionX-1;
                break;
            }
            case NORTH:{
                positionY = positionY + 1;
                break;
            }
            case SOUTH:{
                positionY = positionY -1;
                break;
            }
        }
    }
}
