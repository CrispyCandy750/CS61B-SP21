package byow.Core;

import byow.Core.version2.Direction;
import byow.Core.version2.Position;
import byow.Core.version2.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.lang.annotation.Target;
import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85;
    public static final int HEIGHT = 48;


    //    public static final Random RANDOM = new Random(123456);
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final TETile WALL_TILE = Tileset.WALL;
    private static final TETile FLOOR_TILE = Tileset.FLOOR;
    private static final TETile LOCKED_DOOR_TILE = Tileset.LOCKED_DOOR;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        Random random = new Random(parseInput(input));
        TETile[][] world = generateWorldFilledWithRooms(random, WIDTH, HEIGHT, WALL_TILE,
                FLOOR_TILE);

        return world;
    }

    /** Parse the input */
    private static long parseInput(String input) {
        input = input.toUpperCase();
        return parseSeed(input);
    }

    /**
     * Parse the seed number.
     *
     * @param input a string must start with S.
     */
    private static long parseSeed(String input) {
        return Long.parseLong(input.substring(1, input.indexOf('S')));
    }

    /** Returns a world filled with random rooms. */
    private static TETile[][] generateWorldFilledWithRooms(Random random, int width, int height,
            TETile wall, TETile floor
    ) {
        TETile[][] world = new TETile[width][height];
        fillWithBlankTiles(world);
        fillWithRoomRandomly(random, world, wall, floor);
        return world;
    }

    /** fill the room in the 2D TETile matrix. */
    public static void fillWithRoomRandomly(Random random, TETile[][] tiles, TETile wall,
            TETile floor
    ) {
        Room firstRoom = Room.getFirstRoom(random, tiles);
        BFSSpread(random, tiles, firstRoom);
        openUpALockedDoor(random, tiles, firstRoom, WALL_TILE, LOCKED_DOOR_TILE);
    }

    /** Spread the room with BFS. */
    private static void BFSSpread(Random random, TETile[][] tiles, Room firstRoom) {
        Queue<Room> roomQueue = new LinkedList<>();
        List<Room> addedRooms = new ArrayList<>();
        roomQueue.add(firstRoom);

        while (!roomQueue.isEmpty()) {
            Room room = roomQueue.poll();

            if (isDrawable(tiles, addedRooms, room)) {
                Room.drawRoomToTiles(tiles, room, WALL_TILE, FLOOR_TILE);
                //                roomQueue.addAll(room.getAllNeighborList(random));
                roomQueue.addAll(Room.generateNewNeighborsList(random, room));
                addedRooms.add(room);
            }
        }
    }

    /** Open up a random locked door in the given room. */
    private static void openUpALockedDoor(Random random, TETile[][] tiles, Room room,
            TETile wallTile, TETile lockedDoorTile
    ) {
        Position randomLockedDoor = getRandomLockedDoor(random, tiles, room, wallTile);
        DrawUtils.drawATile(tiles, randomLockedDoor, lockedDoorTile);
    }

    /** get a random door in the given room. */
    private static Position getRandomLockedDoor(Random random, TETile[][] tiles, Room room,
            TETile wall
    ) {
        Direction[] directions = Direction.values();
        Direction doorDirection = directions[RandomUtils.uniform(random, 0, directions.length)];
        return room.getRandomWallTile(random, tiles, doorDirection, wall);
    }

    /**
     * Returns true if the room can be drawn in the tiles.
     * Returns false if the room is out of the tiles or is overlap with an added room.
     */
    private static boolean isDrawable(TETile[][] tiles, List<Room> addedRooms, Room room) {
        return !Room.isOutOfBound(tiles, room) && !haveConflict(addedRooms, room);
    }

    /** Returns true if the room is overlap with the room in the `addedRooms` */
    private static boolean haveConflict(List<Room> addedRooms, Room room) {
        for (Room addedRoom : addedRooms) {
            if (Room.haveConflict(addedRoom, room)) {
                return true;
            }
        }
        return false;
    }

    /** Fills the given 2D array of tiles with given tiles. */
    public static void fillWithBlankTiles(TETile[][] tiles) {
        fillWithGivenTiles(tiles, Tileset.NOTHING);
    }

    /** Fills the given 2D array of tiles with given tiles. */
    public static void fillWithGivenTiles(TETile[][] tiles, TETile tile) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = tile;
            }
        }
    }
}
