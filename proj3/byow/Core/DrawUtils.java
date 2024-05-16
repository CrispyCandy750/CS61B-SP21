package byow.Core;

import byow.Core.version2.Position;
import byow.TileEngine.TETile;

public class DrawUtils {

    /** Draw a vertical line in the 2D tile matrix at given position with given tile. */
    public static void drawHorizontalLine(TETile[][] tiles, Position leftPoint, int length,
            TETile tile
    ) {
        for (int dx = 0; dx < length; dx++) {
            tiles[leftPoint.x + dx][leftPoint.y] = tile;
        }
    }

    /** Draw a col in the 2D tile matrix at given position with given tile. */
    public static void drawVerticalLine(TETile[][] tiles, Position bottomPoint, int length,
            TETile tile
    ) {
        for (int dy = 0; dy < length; dy++) {
            tiles[bottomPoint.x][bottomPoint.y + dy] = tile;
        }
    }

    /** Draw a tile at the given position with given tile. */
    public static void drawATile(TETile[][] tiles, Position position, TETile tile) {
        tiles[position.x][position.y] = tile;
    }
}
