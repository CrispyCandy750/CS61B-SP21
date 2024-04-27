package byow.lab12;

import byow.TileEngine.Point;
import byow.TileEngine.TETile;

/** Represents a hexagon. */
class Hexagon {


    /** Returns the height of the hexagon with the specific side length. */
    static int getHeight(int sideLength) {
        return sideLength * 2;
    }

    /** Returns the width of the hexagon with the specific side length.  */
    static int getWidth(int sideLength) {
        return sideLength * 3 - 2;
    }

    /* ------------------------ instance variable & method ------------------------ */

    final int height;
    final int width;

    Hexagon(int sideLength) {
        this.width = getWidth(sideLength);
        this.lineLength = calLineLength(sideLength);
        this.offsetX = calOffsetX(sideLength);
        this.height = getHeight(sideLength);
    }

    /** Adds this hexagon to the world at the specific position. */
    void addHexagonToWorld(TETile[][] tiles, Point leftBottomPoint, TETile tile) {

        /* populate the world row by row. */
        for (int row = 0; row < height; row++) {
            Point leftPoint = new Point(leftBottomPoint.x + offsetX[row], leftBottomPoint.y + row);
            addLineToWorld(tiles, leftPoint, lineLength[row], tile);
        }
    }

    /* ------------------------ private variable & method ------------------------ */

    /** The length of each row of the hexagon with specific side length. */
    private final int lineLength[];

    /** The offset x of each row of the hexagon with specific side length. */
    private final int offsetX[];

    /** Calculate the line length of each row. */
    private int[] calLineLength(int sideLength) {
        int[] lineLength = new int[getHeight(sideLength)];

        /* populate the line length. */
        for (int row = 0, baseLength = sideLength;
             row < lineLength.length / 2; row++, baseLength += 2) {

            /* The line length of hexagon are symmetric. */
            lineLength[row] = lineLength[lineLength.length - 1 - row] = baseLength;
        }

        return lineLength;
    }

    /** Calculate the offset x of each row. */
    private int[] calOffsetX(int sideLength) {
        int[] offsetX = new int[getHeight(sideLength)];

        /* populate the offset x. */
        for (int row = (offsetX.length - 1) / 2, baseOffsetX = 0;
             row >= 0; row--, baseOffsetX += 1) {
            offsetX[row] = offsetX[offsetX.length - 1 - row] = baseOffsetX;
        }

        return offsetX;
    }

    /** Add a line with specific length to the world based on the left point. */
    private void addLineToWorld(TETile[][] tiles, Point leftPoint, int length, TETile tile) {
        for (int x = leftPoint.x, i = 0, y = leftPoint.y;
             i < length; i++, x++) {
            tiles[x][y] = tile;
        }
    }
}
