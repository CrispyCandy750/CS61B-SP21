package byow.Core.test;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;

import java.util.Random;

public class TilesetTest {


    private static final int WORLD_WIDTH = 85;
    private static final int WORLD_HEIGHT = 48;

    //    private static final Random RANDOM = Engine.RANDOM;
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

    //////////////////////////////////////////////////////////////////////
    // helper function
    //////////////////////////////////////////////////////////////////////

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
