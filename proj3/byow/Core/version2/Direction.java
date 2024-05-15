package byow.Core.version2;

import byow.Core.RandomUtils;

import java.util.Random;

/** Represents the direction. */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    /** Returns the opposite direction of the given direction. */
    public static Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default: return null;
        }
    }

    /** Returns the vertical direction of the given direction */
    public static Direction getVerticalDirection(Direction direction) {
        switch (direction) {
            case UP: case DOWN:
                return RIGHT;
            case LEFT: case RIGHT:
                return UP;
        }
        return null;
    }
}
