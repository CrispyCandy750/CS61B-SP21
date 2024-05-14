package byow.Core.genration;

import byow.TileEngine.TETile;

/** Represents a hallway in the 2D tile matrix. */
public interface Hallway {

    /** Draw this hallway in the 2D tile matrix. */
    void drawToTiles(TETile[][] tiles, TETile wall, TETile floor);

    /**
     * Returns the hallway connect two points.
     * The param point1 and point2 must have direction.
     */
    public static Hallway getHallway(Position point1, Position point2) {
        if (point1.direction == null || point2.direction == null) {
            return null;
        }
        return null;
    }
}
