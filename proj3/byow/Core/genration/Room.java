package byow.Core.genration;


import byow.Core.Engine;
import byow.Core.RandomUtils;

import java.util.Random;

/** Represents a room "in the tile world" which contains the border. */
public class Room {

    /** Returns a random-size room anchored at given position. */
    public static Room getInstance(Position position) {
        return new Room(position, randomWidth(), randomHeight());
    }

    public static Room getInstance(Position position, int width, int height) {
        return new Room(position, width, height);
    }

    /* ------------------------ private static variable & method ------------------------ */
    private static final double MAX_RATIO = 0.25;
    private static final double MIN_RATIO = 0.1;
    private static final Random RANDOM = Engine.RANDOM;

    /** The least length of floor must be 1. */
    private static final int MIN_LENGTH = 3;

    private static final int MAX_WIDTH = Math.max((int) (Engine.WIDTH * MAX_RATIO), MIN_LENGTH);
    private static final int MIN_WIDTH = Math.max((int) (Engine.WIDTH * MIN_RATIO), MIN_LENGTH);
    private static final int MAX_HEIGHT = Math.max((int) (Engine.HEIGHT * MAX_RATIO), MIN_LENGTH);
    private static final int MIN_HEIGHT = Math.max((int) (Engine.HEIGHT * MIN_RATIO), MIN_LENGTH);

    /** Returns a random width of the room. */
    private static int randomWidth() {
        return randomLength(MIN_WIDTH, MAX_WIDTH + 1);
    }

    /** Returns a random height of the room. */
    private static int randomHeight() {
        return randomLength(MIN_HEIGHT, MAX_HEIGHT + 1);
    }

    /** Returns a random integer uniformly in [min, max). */
    private static int randomLength(int min, int max) {
        return RandomUtils.uniform(RANDOM, min, max);
    }

    /* ------------------------ public instance variable & method ------------------------ */


    public Position getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("%s, w:%d, h:%d", position, width, height);
    }

    /** Returns the border at the given direction. */
    public int getBorderLine(Direction direction) {
        switch (direction) {
            case UP:
                return position.y + height - 1;
            case DOWN:
                return position.y;
            case LEFT:
                return position.x;
            case RIGHT:
                return position.x + width - 1;
            default: return 0;
        }
    }

    /** Returns a random entry position with direction. */
    public Position getRandomEntrance(Random random, Direction... directions) {

        /* Get the direction of the entrance. */
        Direction entranceDir = Direction.getRandomDirection(random, directions);
        return getRandomEntrance(random, entranceDir);
    }

    /** Returns true if the destination is reachable for the room. */
    public boolean isReachable(Position destination) {
        switch (destination.direction) {
            case DOWN: case UP:
                return destination.x != getBorderLine(Direction.LEFT)
                        && destination.x != getBorderLine(Direction.RIGHT);
            case LEFT: case RIGHT:
                return destination.y != getBorderLine(Direction.UP)
                        && destination.y != getBorderLine(Direction.DOWN);
            default: return false;
        }
    }

    /**
     * Returns a random entrance to the given destination.
     * Returns null if the destination is not reachable.
     */
    public Position getRandomEntrance(Random random, Position destination) {
        if (!isReachable(destination)) {
            return null;
        }

        boolean roomIsAbovePoint = isAbove(destination);
        boolean roomIsUnderPoint = isUnder(destination);
        boolean roomIsToTheLeftOfPoint = isToTheLeftOf(destination);
        boolean roomIsToTheRightOfPoint = isToTheRightOf(destination);

        switch (destination.direction) {
            case UP: case DOWN:
                if (roomIsToTheLeftOfPoint) {
                    return getRandomEntrance(random, Direction.RIGHT);
                } else if (roomIsToTheRightOfPoint) {
                    return getRandomEntrance(random, Direction.LEFT);
                } else {
                    Direction entranceDir = Direction.getOppositeDirection(
                            destination.direction);
                    return new Position(destination.x, getBorderLine(entranceDir), entranceDir);
                }
            case LEFT: case RIGHT:
                if (roomIsAbovePoint) {
                    return getRandomEntrance(random, Direction.DOWN);
                } else if (roomIsUnderPoint) {
                    return getRandomEntrance(random, Direction.UP);
                } else {
                    Direction entranceDir = Direction.getOppositeDirection(
                            destination.direction);
                    return new Position(getBorderLine(entranceDir), destination.y, entranceDir);
                }
        }
        return null;
    }

    /* ------------------------ private instance variable & method ------------------------ */
    /** The bottom-left point of the room. */
    private Position position;
    /** The width of the room contains the border. */
    private final int width;
    /** The height of the room contains the border. */
    private final int height;

    private Room(Position position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /** Returns a random entry position with direction. */
    private Position getRandomEntrance(Random random, Direction direction) {
        switch (direction) {
            case UP: case DOWN:
                return new Position(RandomUtils.uniform(random, getBorderLine(Direction.LEFT) + 1,
                        getBorderLine(Direction.RIGHT) - 1),
                        getBorderLine(direction), direction);
            case LEFT: case RIGHT:
                return new Position(getBorderLine(direction),
                        RandomUtils.uniform(random, getBorderLine(Direction.DOWN) + 1,
                                getBorderLine(Direction.UP) - 1), direction);
            default: return null;
        }
    }

    /**
     * Return true if the point is within the range at given direction.
     * e.g. if the direction is UP, returns true if the point.y in (left_border, right_border)
     */
    private boolean isWithinRange(Direction direction, Position point) {
        switch (direction) {
            case UP: case DOWN:
                return Position.isWithinHorizontally(position, getBorderLine(Direction.LEFT),
                        getBorderLine(Direction.RIGHT));
            case LEFT: case RIGHT:
                return Position.isWithinVertically(position, getBorderLine(Direction.DOWN),
                        getBorderLine(Direction.UP));
            default: return false;
        }
    }

    /** Returns true if the room is under the point, false otherwise. */
    private boolean isUnder(Position position) {
        return Position.isAbove(position, getBorderLine(Direction.UP));
    }

    /** Returns true if the room is above the point, false otherwise. */
    private boolean isAbove(Position position) {
        return Position.isUnder(position, getBorderLine(Direction.DOWN));
    }

    /** Returns true if the room is to the left of the position, false otherwise. */
    private boolean isToTheLeftOf(Position position) {
        return Position.isToTheRightOf(position, getBorderLine(Direction.RIGHT));
    }

    /** Returns true if the room is to the right of the position, false otherwise. */
    private boolean isToTheRightOf(Position position) {
        return Position.isToTheLeftOf(position, getBorderLine(Direction.LEFT));
    }
}
