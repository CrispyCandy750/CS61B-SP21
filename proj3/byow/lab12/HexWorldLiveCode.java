package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.logging.Level;

public class HexWorldLiveCode {
    private static final int WIDTH = 70;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world);

        ter.renderFrame(world);
    }

    /* ------------------------ private static variable & method ------------------------ */

    private static void drawWorld(TETile[][] world) {
        fillWithBlankTiles(world);
        tesselateHexagons(world, 4);
    }

    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void moveUp() {
            moveUp(1);
        }

        void moveUp(int dy) {
            y += dy;
        }

        void moveRight() {
            moveRight(1);
        }

        void moveRight(int dx) {
            x += dx;
        }

        /** Returns a new position based on this position and the bias (dx, dy) */
        Position shift(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }

        /** Returns a new position based on this position and the bias (dx, 0) */
        Position shiftX(int dx) {
            return new Position(x + dx, y);
        }

        /** Returns a new position based on this position and the bias (0, dy) */
        Position shiftY(int dy) {
            return new Position(x, y + dy);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    /**
     * Tesselate a hexagon with side length SIDE_LENGTH into the world anchored at the given
     * position with given tile.
     */
    private static void tesselateHexagon(TETile[][] world, Position leftBottomPoint,
            int sideLength, TETile tile
    ) {
        tesselateHexagonHelper(world, leftBottomPoint, sideLength - 1, sideLength, tile);
    }

    /** Tesselate a line into the matrix. */
    private static void tesselateALine(TETile[][] world, Position leftPoint, int length,
            TETile tile
    ) {
        for (int dx = 0; dx < length; dx++) {
            world[leftPoint.x + dx][leftPoint.y] = tile;
        }
    }

    /**
     * The helper function of tesselateHexagon() which tesselates hexagon row by row recursively
     * .
     */
    private static void tesselateHexagonHelper(TETile[][] world, Position leftPoint,
            int offsetX, int length, TETile tile
    ) {
        if (offsetX < 0) {
            return;
        }

        tesselateALine(world, leftPoint.shiftX(offsetX), length, tile);
        leftPoint.moveUp();

        tesselateHexagonHelper(world, leftPoint, offsetX - 1,
                length + 2, tile);

        tesselateALine(world, leftPoint.shiftX(offsetX), length, tile);
        leftPoint.moveUp();
    }

    /**
     * Fills the given 2D array of tiles with blank tiles.
     *
     * @param tiles
     */
    private static void fillWithBlankTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Returns a random tile. */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.WATER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.SAND;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    /** Returns the height of the hexagon with given side length. */
    private static int getHeightOfHexagon(int sideLength) {
        return 2 * sideLength;
    }

    /** Returns the width of the hexagon with given side length. */
    private static int getWidthOfHexagon(int sideLength) {
        return 3 * sideLength - 2;
    }

    /** Returns the offsetY of two columns. */
    private static int getOffsetOfTwoColumn(int sideLength) {
        return 2 * sideLength - 1;
    }

    /** Tesselates all hexagons into the world. */
    private static void tesselateHexagons(TETile[][] world, int sideLength) {
        tesselateHexagonsHelper(world, new Position(0, 0), getHeightOfHexagon(sideLength),
                sideLength, 3);
    }

    /** Tesselates the columns of hexagons recursively. */
    private static void tesselateHexagonsHelper(TETile[][] world, Position leftBottomPoint,
            int offsetY, int sideLength, int num
    ) {

        /* The offset of two Adjacent columns. */
        int offsetXOfTwoColumns = getOffsetOfTwoColumn(sideLength);

        tesselateColOfHexagons(world, leftBottomPoint.shiftY(offsetY), sideLength, num);
        leftBottomPoint.moveRight(offsetXOfTwoColumns);

        if (offsetY == 0) {
            return;
        }

        tesselateHexagonsHelper(world, leftBottomPoint,
                offsetY - getHeightOfHexagon(sideLength) / 2, sideLength, num + 1);

        tesselateColOfHexagons(world, leftBottomPoint.shiftY(offsetY), sideLength, num);
        leftBottomPoint.moveRight(offsetXOfTwoColumns);
    }


    /**
     * Tesselates a column of hexagons into the world from bottom up starting at the give
     * position.
     */
    private static void tesselateColOfHexagons(TETile[][] world, Position leftBottomPoint,
            int sideLength, int num
    ) {
        int heightOfHexagon = getHeightOfHexagon(sideLength);

        for (int i = 0; i < num; i++) {
            tesselateHexagon(world, leftBottomPoint.shiftY(i * heightOfHexagon), sideLength,
                    randomTile());
        }
    }
}
