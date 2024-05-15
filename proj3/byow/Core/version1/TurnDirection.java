package byow.Core.version1;

/** The direction of turn hallway. */
public enum TurnDirection {
    /**
     * Four direction represents distinct turn hallway:
     * __               |            |                  ___
     * |: RIGHT_DOWN   |: RIGHT_UP  |   : DOWN_RIGHT   |  : UP_RIGHT
     * |             __|            |___               |
     */

    RIGHT_UP, RIGHT_DOWN, UP_RIGHT, DOWN_RIGHT;

    /** Returns the turning point between two points. */
    public static Position getTurningPoint(Position leftPoint, Position rightPoint,
            TurnDirection turnDirection
    ) {
        switch (turnDirection) {
            case RIGHT_UP: case RIGHT_DOWN:
                return new Position(rightPoint.x, leftPoint.y);
            case UP_RIGHT: case DOWN_RIGHT:
                return new Position(leftPoint.x, rightPoint.y);
            default: return null;
        }
    }

    public static TurnDirection getOppositeDirection(TurnDirection turnDirection) {
        switch (turnDirection) {
            case RIGHT_UP: return UP_RIGHT;
            case DOWN_RIGHT: return RIGHT_DOWN;
            case UP_RIGHT: return RIGHT_UP;
            case RIGHT_DOWN: return DOWN_RIGHT;
            default: return null;
        }
    }
}
