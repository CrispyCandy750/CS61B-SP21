package byow.Core.genration;


import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;

import java.util.Random;

/** Represents a room "in the tile world" which contains the border. */
public class Room {

    /** Returns a random-size room anchored at given position. */
    public static Room getInstance(Position position) {
        return new Room(position, randomWidth(), randomHeight());
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
}
