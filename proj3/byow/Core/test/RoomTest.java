package byow.Core.test;

import byow.Core.Engine;
import byow.Core.version2.Direction;
import byow.Core.version2.Position;
import byow.Core.version2.Room;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Test the method of the Room class. */
public class RoomTest {

    private static final int WORLD_WIDTH = 85;
    private static final int WORLD_HEIGHT = 48;

//        private static final Random RANDOM = Engine.RANDOM;
    public static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final long SLEEP_TIME = 500000;

    private static TETile[][] world;
    private static TERenderer ter;

    /* Basic preparation. */
    static {
        world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
        fillWithBlankTiles(world);
        ter = new TERenderer();
        ter.initialize(world.length, world[0].length);
    }

    @Test
    /** Test the Room.attachNeighbor() method. */
    public void attachNeighbor() throws InterruptedException {
        Room room = new Room(new Position(30, 15), 20, 15);

        Room neighbor = new Room(10, 5);
        Room.attachNeighbor(room, Direction.DOWN, neighbor, 5);

        Room.drawRoomToTiles(world, room, Tileset.WALL, Tileset.FLOOR);
        Room.drawRoomToTiles(world, neighbor, Tileset.WALL, Tileset.FLOOR);
        ter.renderFrame(world);


        Thread.sleep(SLEEP_TIME);
    }

    @Test
    /** Test the Room.attachNeighborRandomly() method. */
    public void attachNeighborRandomly() throws InterruptedException {
        Room room = new Room(new Position(30, 15), 20, 15);

        Room neighbor = new Room(10, 5);
        Room.attachNeighborRandomly(RANDOM, room, Direction.RIGHT, neighbor);

        Room.drawRoomToTiles(world, room, Tileset.WALL, Tileset.FLOOR);
        Room.drawRoomToTiles(world, neighbor, Tileset.WALL, Tileset.FLOOR);
        ter.renderFrame(world);


        Thread.sleep(SLEEP_TIME);
    }

    @Test
    /** Test the Room.generateNeighbor() method. */
    public void generateNeighbor() throws InterruptedException {
        Room room = new Room(new Position(30, 15), 8, 8);
        Room neighbor = Room.generateNeighbor(RANDOM, room, Direction.RIGHT);
        Room neighbor2 = Room.generateNeighbor(RANDOM, neighbor, Direction.RIGHT);
        Room.drawRoomToTiles(world, room, Tileset.WALL, Tileset.FLOOR);
        Room.drawRoomToTiles(world, neighbor, Tileset.WALL, Tileset.FLOOR);
        Room.drawRoomToTiles(world, neighbor2, Tileset.WALL, Tileset.FLOOR);
        ter.renderFrame(world);

        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void isOverlap() {
        Room room1 = new Room(new Position(30, 15), 8, 8);
        Room room2 = new Room(new Position(30, 15), 4, 4);

        boolean condition = Room.haveConflict(room1, room2);
        assertTrue(condition);
    }

    @Test
    public void fillWithRooms() throws InterruptedException {
        Engine.fillWithRoomRandomly(RANDOM, world);
        ter.renderFrame(world);

        Thread.sleep(SLEEP_TIME);
    }

//    @Test
//    public void spread() throws InterruptedException {
//        Engine.BFSSpread(RANDOM, world, new Room(new Position(0, 0), 40, 30));
//        ter.renderFrame(world);
//
//        Thread.sleep(SLEEP_TIME);
//    }

    @Test
    public void isOutOfBound() {
        Room room1 = new Room(new Position(80, 0), 20, 15);
        boolean condition1 = Room.isOutOfBound(world, room1);
        assertTrue(condition1);

        Room room2 = new Room(new Position(-12, 5), 20, 15);
        boolean condition2 = Room.isOutOfBound(world, room1);
        assertTrue(condition2);

        Room room3 = new Room(new Position(0, 40), 20, 15);
        boolean condition3 = Room.isOutOfBound(world, room3);
        assertTrue(condition3);

        Room room4 = new Room(new Position(0, -10), 20, 30);
        boolean condition4 = Room.isOutOfBound(world, room4);
        assertTrue(condition4);

        for (int i = 0; i < 100; i++) {
            Room room = Room.getFirstRoom(RANDOM, world);
            assertFalse(Room.isOutOfBound(world, room));
        }
    }

    @Test
    public void haveConflict() {
        Room room0 = new Room(new Position(22, 25), 34, 18);
        Room room2 = new Room(new Position(5, 25), 45, 15);
        boolean condition1 = Room.haveConflict(room0, room2);
        assertTrue(condition1);
    }

    //    @Test
    //    public void getEndPoints() {
    //        Room room = new Room(new Position(30, 15), 20, 15);
    //        Position actualBottomLeftPoint = room.getEndPoints(Direction.LEFT, Direction.DOWN);
    //        Position expectBottomLeftPoint = new Position(30, 15);
    //        assertEquals(expectBottomLeftPoint, actualBottomLeftPoint);
    //
    //        Position actualTopLeftPoint = room.getEndPoints(Direction.LEFT, Direction.UP);
    //        Position expectTopLeftPoint = new Position(30, 29);
    //        assertEquals(expectTopLeftPoint, actualTopLeftPoint);
    //
    //        Position actualBottomRightPoint = room.getEndPoints(Direction.RIGHT, Direction.DOWN);
    //        Position expectBottomRightPoint = new Position(49, 15);
    //        assertEquals(expectBottomRightPoint, actualBottomRightPoint);
    //
    //        Position actualTopRightPoint = room.getEndPoints(Direction.RIGHT, Direction.UP);
    //        Position expectTopRightPoint = new Position(49, 29);
    //        assertEquals(expectTopRightPoint, actualTopRightPoint);
    //    }


    /* ------------------------------------------------------------------------------------- */
    /* ---------------------------------- helper function ---------------------------------- */
    /* ------------------------------------------------------------------------------------- */

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
