package byow.TileEngine;

import java.awt.Color;
import java.util.Iterator;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");


    ////////////////////////////////////////////////////
    // The class below is made for debugging
    ////////////////////////////////////////////////////

    public static Iterable<TETile> getNumberIterable() {
        return new Iterable<TETile>() {
            @Override
            public Iterator<TETile> iterator() {
                return new NumberTileIterator();
            }
        };
    }

    private static class NumberTileIterator implements Iterator<TETile> {

        static final Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, Color.PINK};
        static final char[] numbers = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        private int curColor;
        private int curNumber;

        NumberTileIterator() {
            curColor = curNumber = 0;
        }

        @Override
        public boolean hasNext() {
            return curColor < colors.length;
        }

        @Override
        public TETile next() {
            TETile res = new TETile(numbers[curNumber], colors[curColor], Color.black,
                    numbers[curNumber] + "");
            moveCur();
            return res;
        }

        /** Move the point to the next valid position. */
        private void moveCur() {
            curNumber++;
            if (curNumber >= numbers.length) {
                curNumber = 0;
                curColor++;
            }
        }
    }
}


