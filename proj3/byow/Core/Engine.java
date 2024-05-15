package byow.Core;

import byow.Core.version2.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static final Random RANDOM = new Random(123456);
//    public static final Random RANDOM = new Random(System.currentTimeMillis());

    public static final TETile WALL_TILE = Tileset.WALL;
    public static final TETile FLOOR_TILE = Tileset.FLOOR;


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

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    /** fill the room in the 2D TETile matrix. */
    public static void fillWithRoomRandomly(TETile[][] tiles) {
        Room firstRoom = Room.getFirstRoom(RANDOM, tiles);
        spread(tiles, firstRoom);
    }

    /** Spread the room with WFS. */
    public static void spread(TETile[][] tiles, Room firstRoom) {
        Queue<Room> roomQueue = new LinkedList<>();
        List<Room> addedRooms = new ArrayList<>();
        roomQueue.add(firstRoom);

        // for debug
        Iterator<TETile> iterator = Tileset.getNumberIterable().iterator();
        int index = 0;

        while (!roomQueue.isEmpty()) {
            Room room = roomQueue.poll();

            if (isDrawable(tiles, addedRooms, room)) {
                Room.drawRoomToTiles(tiles, room, WALL_TILE, FLOOR_TILE);
                roomQueue.addAll(room.getAllNeighborList(RANDOM));
                addedRooms.add(room);
                System.out.println(room + " √ " + (index++));
            } else if (!Room.isOutOfBound(tiles, room)) {
                System.out.println(room + " ×");
            } else {
                System.out.println(room + " ×");
            }
        }
    }

    /**
     * Returns true if the room can be drawn in the tiles.
     * Returns false if the room is out of the tiles or is overlap with an added room.
     */
    private static boolean isDrawable(TETile[][] tiles, List<Room> addedRooms, Room room) {
        return !Room.isOutOfBound(tiles, room) && !isOverlap(addedRooms, room);
    }

    /** Returns true if the room is overlap with the room in the `addedRooms` */
    private static boolean isOverlap(List<Room> addedRooms, Room room) {
        for (Room addedRoom : addedRooms) {
            if (Room.haveConflict(addedRoom, room)) {
                return true;
            }
        }
        return false;
    }

}
