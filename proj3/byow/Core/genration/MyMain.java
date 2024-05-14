package byow.Core.genration;

import byow.Core.DrawUtils;
import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/** My own Main class to test the interactWithInputString() method. */
public class MyMain {

    private static final int WORLD_WIDTH = 85;
    private static final int WORLD_HEIGHT = 48;

    private static final Random RANDOM = Engine.RANDOM;

    public static void main(String[] args) {

        TETile[][] world = new TETile[WORLD_WIDTH][WORLD_HEIGHT];
        fillWithGivenTiles(world, Tileset.NOTHING);

        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);

        drawWorld(world);

        ter.renderFrame(world);
    }

    /** Draw the world with room. */
    public static void drawWorld(TETile[][] world) {
        fillWithBlankTiles(world);
        Room room1 = Room.getInstance(new Position(0, 0));
        Room room2 = Room.getInstance(new Position(10, 8));
        Engine.addRoom(world, room1);
        Engine.addRoom(world, room2);

        Position entrance1 = room2.getRandomEntrance(RANDOM, Direction.DOWN);
        Position entrance2 = room1.getRandomEntrance(RANDOM, entrance1);

        world[entrance1.x][entrance1.y] = Tileset.FLOWER;
        world[entrance2.x][entrance2.y] = Tileset.FLOWER;
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
