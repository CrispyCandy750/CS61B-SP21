package byow.Core.version2;

import byow.Core.DrawUtils;
import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Represents a room "in the tile world" which contains the border. */
public class Room {

    /**
     * Returns the first room in the tiles, the width and height of the floor in this room are
     * more than 1 for opening up a locked door.
     */
    public static Room getFirstRoom(Random random, TETile[][] tiles) {
        Room room = getFirstRoomWithSize(random);

        int positionX = RandomUtils.uniform(random, tiles.length - room.width);
        int positionY = RandomUtils.uniform(random, tiles[0].length - room.height);
        room.setPosition(new Position(positionX, positionY));

        return room;
    }

    /**
     * Generate a neighbor of the room at the given direction.
     *
     * @param direction the direction of the neighbor relative to given room.
     * e.g. if direction is LEFT, the neighbor is to the left of the room.
     */
    public static Room generateNeighbor(Random random, Room room, Direction direction) {
        Room neighbor = generateRandomNeighborWithSize(random, room, direction);

        /* locate the neighbor randomly. */
        attachNeighborRandomly(random, room, direction, neighbor);

        openUpADoorInNeighbor(room, direction, neighbor);

        return neighbor;
    }

    /**
     * Return true if the two rooms has conflict, false otherwise.
     * (Neighbors has no conflict, i.e. if room1 and room2 is neighbors, returns true)
     */
    public static boolean haveConflict(Room room1, Room room2) {
        return !Room.isNeighbor(room1, room2) && Room.isOverlap(room1, room2);
    }

    /** Returns true if the room is out of the bound of the tiles. */
    public static boolean isOutOfBound(TETile[][] tiles, Room room) {
        return room.getBorderPosition(Direction.DOWN) < 0
                || room.getBorderPosition(Direction.UP) >= tiles[0].length
                || room.getBorderPosition(Direction.LEFT) < 0
                || room.getBorderPosition(Direction.RIGHT) >= tiles.length;
    }

    /** Draw the given room in the 2D tile matrix. */
    public static void drawRoomToTiles(TETile[][] tiles, Room room, TETile wall, TETile floor) {
        drawRoomToTilesWithoutDoor(tiles, room, wall, floor);
        if (room.door != null) {
            //            Door.drawDoorToTiles(tiles, room.door, Tileset.FLOWER);
            Door.drawDoorToTiles(tiles, room.door, floor);
        }
    }

    /**
     * Returns of a list containing new neighbors of room except the direction of the first
     * neighbor.
     */
    public static List<Room> generateNewNeighborsList(Random random, Room room) {
        ArrayList<Room> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (!direction.equals(room.firstNeighborDirection)) {
                neighbors.add(Room.generateNeighbor(random, room, direction));
            }
        }
        return neighbors;
    }

    ////////////////////////////////////////////////////////////////
    // private static variable & method
    ////////////////////////////////////////////////////////////////
    private static final double MAX_RATIO = 0.25;
    private static final double MIN_RATIO = 0.1;

    /** The least length of floor must be 1, this is also the width of hallway. */
    private static final int LEN_HALLWAY = 3;

    private static final int MAX_WIDTH = Math.max((int) (Engine.WIDTH * MAX_RATIO), LEN_HALLWAY);
    private static final int MIN_WIDTH = Math.max((int) (Engine.WIDTH * MIN_RATIO), LEN_HALLWAY);
    private static final int MAX_HEIGHT = Math.max((int) (Engine.HEIGHT * MAX_RATIO), LEN_HALLWAY);
    private static final int MIN_HEIGHT = Math.max((int) (Engine.HEIGHT * MIN_RATIO), LEN_HALLWAY);

    /** Returns a random width of the room. */
    private static int getRandomWidth(Random random) {
        return getRandomLength(random, MIN_WIDTH, MAX_WIDTH + 1);
    }

    /** Returns a random height of the room. */
    private static int getRandomHeight(Random random) {
        return getRandomLength(random, MIN_HEIGHT, MAX_HEIGHT + 1);
    }

    /** Returns a random integer uniformly in [min, max). */
    private static int getRandomLength(Random random, int min, int max) {
        return RandomUtils.uniform(random, min, max);
    }

    /**
     * Open up a door In neighbor room (i.e. set the door variable of neighbor).
     *
     * @param direction the direction of the neighbor relative to the given room.
     */
    private static void openUpADoorInNeighbor(Room room, Direction direction, Room neighbor) {
        Door door = Door.generateDoor(room, direction, neighbor);
        neighbor.setDoor(door);
    }

    /**
     * Attach the neighbor to the given direction of the room randomly.
     * i.e. set the position variable of the neighbor and add the neighbor to the `neighbors` map.
     *
     * @param direction the direction of the neighbor relative to the given room.
     * @param neighbor the neighbor without position (i.e. the position var is null).
     */
    public static void attachNeighborRandomly(Random random, Room room, Direction direction,
            Room neighbor
    ) {
        int randomPosDelta = getRandomPosDelta(random, room, direction, neighbor);
        attachNeighbor(room, direction, neighbor, randomPosDelta);
    }

    /**
     * Attach the neighbor to the given direction of the room.
     * i.e. set the position variable of the neighbor and add the neighbor to the `neighbors` map.
     * This method is made for {@method attachNeighborRandomly}
     *
     * @param direction the direction of the neighbor relative to the given room.
     * @param neighbor the neighbor without position (i.e. the position var is null).
     * @param posDelta The positional difference between room and neighbor based on direction.
     */
    public static void attachNeighbor(Room room, Direction direction, Room neighbor, int posDelta
    ) {
        int positionX = 0, positionY = 0;
        switch (direction) {
            case UP:
                positionX = room.getBorderPosition(Direction.LEFT) + posDelta;
                positionY = room.getBorderPosition(Direction.UP);
                break;
            case DOWN:
                positionX = room.getBorderPosition(Direction.LEFT) + posDelta;
                positionY = room.getBorderPosition(Direction.DOWN) - neighbor.height + 1;
                break;
            case LEFT:
                positionX = room.getBorderPosition(Direction.LEFT) - neighbor.width + 1;
                positionY = room.getBorderPosition(Direction.DOWN) + posDelta;
                break;
            case RIGHT:
                positionX = room.getBorderPosition(Direction.RIGHT);
                positionY = room.getBorderPosition(Direction.DOWN) + posDelta;
        }

        neighbor.setPosition(new Position(positionX, positionY));
        /* Set the room to the first neighbor of the `neighbor`. */
        neighbor.addNeighbor(Direction.getOppositeDirection(direction), room);
    }

    /**
     * Returns the positional difference between room and neighbor based on direction
     * This difference will prevent the two rooms from being misaligned.
     * <p>
     * This method is made for {@method attachNeighborRandomly}
     */
    private static int getRandomPosDelta(Random random, Room room, Direction direction,
            Room neighbor
    ) {
        int lenOfAdjacentBoundaryOfRoom = room.getBorderLen(direction);
        int lenOfAdjacentBoundaryOfNeighbor = neighbor.getBorderLen(direction);
        int difference = lenOfAdjacentBoundaryOfRoom - lenOfAdjacentBoundaryOfNeighbor;

        int leftEndPoint = Math.min(0, difference);
        int rightEndPoint = Math.max(0, difference);

        return RandomUtils.uniform(random, leftEndPoint, rightEndPoint + 1);
    }

    /** Draw the given room in the 2D tile matrix but do not draw the door. */
    private static void drawRoomToTilesWithoutDoor(TETile[][] tiles, Room room, TETile wall,
            TETile floor
    ) {
        Position bottomLeftPoint = room.position;
        int width = room.width;
        int height = room.height;

        /* Draw the bottom wall. */
        DrawUtils.drawHorizontalLine(tiles, bottomLeftPoint, width, wall);
        /* Draw the top wall. */
        DrawUtils.drawHorizontalLine(tiles, bottomLeftPoint.shiftY(height - 1), width, wall);
        /* Draw the left wall. */
        DrawUtils.drawVerticalLine(tiles, bottomLeftPoint, height, wall);
        /* Draw the right wall. */
        DrawUtils.drawVerticalLine(tiles, bottomLeftPoint.shiftX(width - 1), height, wall);

        /* Draw the floor. */
        for (int dy = 1, floorWidth = width - 2; dy < height - 1; dy++) {
            DrawUtils.drawHorizontalLine(tiles, bottomLeftPoint.shift(1, dy), floorWidth, floor);
        }
    }

    /**
     * Returns a neighbor only with width and height but not position and location.
     * If {@code room.getBorderLen(direction) > LEN_HALLWAY}, returns a hallway whose width is
     * {@code LEN_HALLWAY}
     */
    private static Room generateRandomNeighborWithSize(Random random, Room room,
            Direction direction
    ) {
        if (room.getBorderLen(direction) > LEN_HALLWAY) {
            return generateRandomHallway(random, direction);
        }
        return getRandomRoomWithSize(random);
    }

    /** Returns a hallway with random length at given direction. */
    private static Room generateRandomHallway(Random random, Direction direction) {
        switch (direction) {
            case UP: case DOWN:
                return new Room(LEN_HALLWAY, getRandomHeight(random));
            case LEFT: case RIGHT:
                return new Room(getRandomWidth(random), LEN_HALLWAY);
        }
        return null;
    }

    /** Returns the room only with size but not position and door. */
    private static Room getRandomRoomWithSize(Random random) {
        return new Room(getRandomWidth(random), getRandomHeight(random));
    }

    /** Returns the first room only with size but not position and door. */
    private static Room getFirstRoomWithSize(Random random) {
        int width, height;
        Room randomRoom = getRandomRoomWithSize(random);
        width = randomRoom.width;
        height = randomRoom.height;
        if (width == LEN_HALLWAY) {
            width++;
        }
        if (height == LEN_HALLWAY) {
            height++;
        }
        return new Room(width, height);
    }

    /** Returns true if the room1 and room2 is neighbors, false otherwise. */
    private static boolean isNeighbor(Room room1, Room room2) {
        return room1.firstNeighbor == room2 || room2.firstNeighbor == room1;
    }

    /** Returns true if the room1 and room2 are overlap, false otherwise. */
    private static boolean isOverlap(Room room1, Room room2) {
        return !(room2.getBorderPosition(Direction.LEFT) > room1.getBorderPosition(Direction.RIGHT)
                || room2.getBorderPosition(Direction.RIGHT) < room1.getBorderPosition(
                Direction.LEFT)
                || room2.getBorderPosition(Direction.UP) < room1.getBorderPosition(Direction.DOWN)
                || room2.getBorderPosition(Direction.DOWN) > room1.getBorderPosition(Direction.UP));
    }

    /** Represents the door of this room, and a door this is only a line. */
    private static class Door {

        /** The left or bottom point of the door line. */
        final Position position;

        /** The direction of the door, UP/RIGHT. */
        final Direction direction;

        /** The length of the door, excluding the wall. */
        int length;

        Door(Position position, Direction direction, int length) {
            this.position = position;
            this.direction = direction;
            this.length = length;
        }

        /**
         * Returns the door between room and neighbor.
         * This method is made for {@method Room.openUpADoorInNeighbor}
         *
         * @param direction the direction of the neighbor relative to the given room.
         * e.g. if direction is LEFT, the neighbor is to the left of room.
         */
        static Door generateDoor(Room room, Direction direction, Room neighbor) {
            int lenOfAdjBoundaryOfRoom = room.getBorderLen(direction);
            int lenOfAdjBoundaryOfNeighbor = neighbor.getBorderLen(direction);

            int length = Math.min(lenOfAdjBoundaryOfNeighbor, lenOfAdjBoundaryOfRoom) - 2;

            /* The direction of door must be vertical with the adjacent direction. */
            Direction doorDirection = Direction.getVerticalDirection(direction);

            Position doorPosition = getPosOfDoor(room, lenOfAdjBoundaryOfRoom, direction, neighbor,
                    lenOfAdjBoundaryOfNeighbor);

            return new Door(doorPosition, doorDirection, length);
        }

        /**
         * Returns the position of the door.
         * This method is made for {@method generateDoor}
         *
         * @param lenOfAdjBoundaryOfRoom The length of side of room at the junction with neighbor.
         * @param lenOfAdjBoundaryOfNeighbor The length of side of neighbor at the junction with
         * room.
         */
        static Position getPosOfDoor(Room room, int lenOfAdjBoundaryOfRoom,
                Direction direction, Room neighbor, int lenOfAdjBoundaryOfNeighbor
        ) {
            if (lenOfAdjBoundaryOfRoom > lenOfAdjBoundaryOfNeighbor) {
                switch (direction) {
                    case UP: return neighbor.getEndPoints(Direction.LEFT, Direction.DOWN)
                            .shiftX(1);
                    case DOWN: return neighbor.getEndPoints(Direction.LEFT, Direction.UP)
                            .shiftX(1);
                    case LEFT: return neighbor.getEndPoints(Direction.RIGHT, Direction.DOWN)
                            .shiftY(1);
                    case RIGHT: return neighbor.getEndPoints(Direction.LEFT, Direction.DOWN)
                            .shiftY(1);
                }
            } else {
                switch (direction) {
                    case UP: return room.getEndPoints(Direction.LEFT, Direction.UP).shiftX(1);
                    case DOWN: return room.getEndPoints(Direction.LEFT, Direction.DOWN).shiftX(1);
                    case LEFT: return room.getEndPoints(Direction.LEFT, Direction.DOWN).shiftY(1);
                    case RIGHT: return room.getEndPoints(Direction.RIGHT, Direction.DOWN).shiftY(1);
                }
            }
            return null;
        }

        /**
         * Draw the given room in the 2D tile matrix.
         *
         * @throws IllegalArgumentException if {@code door == null}
         */
        static void drawDoorToTiles(TETile[][] tiles, Door door, TETile tile) {
            if (door == null) {
                throw new IllegalArgumentException("The door can not be null");
            }
            switch (door.direction) {
                case UP:
                    DrawUtils.drawVerticalLine(tiles, door.position, door.length, tile);
                    break;
                case RIGHT:
                    DrawUtils.drawHorizontalLine(tiles, door.position, door.length, tile);
            }
        }
    }

    private static enum RoomType {
        HALL_WAY, ROOM;
    }

    ////////////////////////////////////////////////////////////////
    // public instance variable & method
    ////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return String.format("%s, w:%d, h:%d", position, width, height);
    }

    /** Returns the position of a random wall tile at given direction. */
    public Position getRandomWallTile(Random random, TETile[][] tiles, Direction direction,
            TETile wall
    ) {
        int positionX = 0, positionY = 0;
        switch (direction) {
            case UP: case DOWN:
                positionY = this.getBorderPosition(direction);
                do {
                    positionX = RandomUtils.uniform(random, this.position.x + 1,
                            getBorderPosition(Direction.RIGHT) - 1);
                } while (!wall.equals(tiles[positionX][positionY]));
                break;
            case LEFT: case RIGHT:
                positionX = this.getBorderPosition(direction);
                do {
                    positionY = RandomUtils.uniform(random, this.position.y + 1,
                            getBorderPosition(Direction.UP) - 1);
                } while (!wall.equals(tiles[positionX][positionY]));
        }
        return new Position(positionX, positionY);
    }

    ////////////////////////////////////////////////////////////////
    // private instance variable & method
    ////////////////////////////////////////////////////////////////
    /** The bottom-left point of the room. */
    private Position position;
    /** The width of the room contains the border. */
    private final int width;
    /** The height of the room contains the border. */
    private final int height;

    /** The door of the room. */
    private Door door;

    private Room firstNeighbor;

    /** The direction of the first neighbor relative to the room. */
    private Direction firstNeighborDirection;

    public Room(int width, int height) {
        this(null, width, height);
    }

    public Room(Position position, int width, int height) {
        this(position, width, height, null);
    }

    private Room(Position position, int width, int height, Door door) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.door = door;
    }

    /** Returns the length of the border at the given direction. */
    private int getBorderPosition(Direction direction) {
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

    /** Returns the position of the border at the given direction. */
    private int getBorderLen(Direction direction) {
        switch (direction) {
            case UP: case DOWN:
                return width;
            case LEFT: case RIGHT:
                return height;
        }
        return 0;
    }

    /**
     * Returns the specific endpoints of the room.
     *
     * @param horDirection horizontal direction: LEFT or RIGHT
     * @param verDirection vertical direction: UP or DOWN
     */
    private Position getEndPoints(Direction horDirection, Direction verDirection) {
        return new Position(getBorderPosition(horDirection), getBorderPosition(verDirection));
    }

    /** Set the position variable. */
    private void setPosition(Position position) {
        this.position = position;
    }

    /** Set the door variable. */
    private void setDoor(Door door) {
        this.door = door;
    }

    /** Add the neighbor at given direction. */
    private void addNeighbor(Direction direction, Room neighbor) {
        this.firstNeighbor = neighbor;
        this.firstNeighborDirection = direction;
    }
}
