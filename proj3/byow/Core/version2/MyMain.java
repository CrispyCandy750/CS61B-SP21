package byow.Core.version2;


import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

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
