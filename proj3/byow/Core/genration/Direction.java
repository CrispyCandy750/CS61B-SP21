package byow.Core.genration;

import byow.Core.RandomUtils;

import java.util.Random;
import java.util.logging.Level;

/** Represents the direction. */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    /** Select a random direction from the given directions. */
    public static Direction getRandomDirection(Random random, Direction... directions) {
        return directions[RandomUtils.uniform(random, 0, directions.length)];
    }

    /** Select a random direction from all directions. */
    public static Direction getRandomDirection(Random random) {
        return getRandomDirection(random, Direction.values());
    }

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
}
