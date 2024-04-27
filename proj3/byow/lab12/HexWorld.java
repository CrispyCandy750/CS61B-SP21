package byow.lab12;

import byow.TileEngine.Point;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Utils.Utils;

import java.util.Iterator;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final long SEED = System.currentTimeMillis();
    private static final Random RANDOM = new Random(SEED);

    private static final int[] hexNumOfEachRow = new int[]{1, 2, 3, 2, 3, 2, 3, 2, 1};

//    private static final int[] hexNumOfEachRow = new int[]{1, 2, 3, 4, 5, 4, 3, 2, 1};

    private static final int sideLength = 5;
    public static void main(String[] args) {

        TETile[][] world = getHexWorld(hexNumOfEachRow, sideLength);

        TERenderer ter = new TERenderer();
        ter.initialize(world.length, world[0].length);

        ter.renderFrame(world);
    }

    /** Returns a world filled with hexagons. */
    private static TETile[][] getHexWorld(int[] hexNumOfEachRow, int sideLength) {
        Hexagon hexagon = new Hexagon(sideLength);
        TETile[][] world = getEmptyHexWorld(hexNumOfEachRow, sideLength);
        for (Point point: getBasePoints(hexNumOfEachRow, sideLength)) {
            hexagon.addHexagonToWorld(world, point, randomTile());
        }
        return world;
    }

    /** Returns a 2D tile array filled with Tileset.NOTHING. */
    private static TETile[][] getEmptyHexWorld(int[] hexNumOfEachRow, int sideLength) {
        int maxHexNum = Utils.getMax(hexNumOfEachRow);
        int width = Hexagon.getWidth(sideLength) * maxHexNum + (maxHexNum - 1) * sideLength;
        int height = Hexagon.getHeight(sideLength) * (hexNumOfEachRow.length + 1) / 2;

        return TETile.getTiles(width, height);
    }
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

    /** Returns the iterable which returns the base points of hexagon one by one. */
    private static Iterable<Point> getBasePoints(int[] hexNumOfEachRow, int sideLength) {
        return () -> new BasePointIterator(hexNumOfEachRow, new Point(0, 0), sideLength);
    }

    /** The left bottom point iterator of each hexagon. */
    private static class BasePointIterator implements Iterator<Point> {

        BasePointIterator(int[] hexNumOfEachRow, Point basePoint, int sideLength) {
            this.hexNumOfEachRow = hexNumOfEachRow;
            this.maxHexNumOfOneRow = Utils.getMax(hexNumOfEachRow);
            this.basePoint = basePoint;
            this.hexOffsetX = Hexagon.getWidth(sideLength) - (sideLength - 1);
            this.hexOffsetY = sideLength;
            this.offsetXOfTwoHexInOneRow = Hexagon.getWidth(sideLength) + sideLength;

            this.curRow = 0;
            this.curNumOfCurRow = 1;
            /* Initialize the first point. */
            this.curPoint = getLeftmostPointOfCurRow();
        }

        @Override
        public boolean hasNext() {
            return curRow < hexNumOfEachRow.length;
        }

        @Override
        public Point next() {
            Point res = curPoint;
            movePoint();
            return res;
        }

        /* ------------------------ private variable & method ------------------------ */

        /** The number of the hexagon of each row. */
        private final int[] hexNumOfEachRow;

        /** The left bottom point of the world. */
        private final Point basePoint;

        /** The max number of hexagon of all rows. */
        private final int maxHexNumOfOneRow;

        private final int hexOffsetX;

        private final int hexOffsetY;

        /** The offset x of two hexagon in one row. */
        private final int offsetXOfTwoHexInOneRow;

        /** The row where the current point is located. */
        private int curRow;

        /** The current point is the nth point in the current row. */
        private int curNumOfCurRow;

        /** The current point. */
        private Point curPoint;

        /** Move the point to the next position. */
        private void movePoint() {
            curNumOfCurRow += 1;
            if (curNumOfCurRow > hexNumOfEachRow[curRow]) {
                curRow++;
                curNumOfCurRow = 1;
            }
            curPoint = getNextPoint();
        }

        /** Returns the next point based the current point in the same row. */
        private Point getNextPoint() {
            if (!hasNext()) {
                return null;
            } else if (curNumOfCurRow == 1) {
                return getLeftmostPointOfCurRow();
            } else {
                return new Point(curPoint.x + offsetXOfTwoHexInOneRow, curPoint.y);
            }
        }

        /** Returns the leftmost point the current row. */
        private Point getLeftmostPointOfCurRow() {
            return new Point(basePoint.x + getLeftMostOffsetXOfCurRow(),
                    basePoint.y + getBottomOffsetYOfCurRow());
        }

        /** Returns the offset of the leftmost point in the current row. */
        private int getLeftMostOffsetXOfCurRow() {
            return (maxHexNumOfOneRow - hexNumOfEachRow[curRow]) * hexOffsetX;
        }

        private int getBottomOffsetYOfCurRow() {
            return hexOffsetY * curRow;
        }
    }
}
