package byow.Core.genration;

import byow.TileEngine.TETile;

/** Represents a hallway in the 2D tile matrix. */
public interface Hallway {

    /** Draw this hallway in the 2D tile matrix. */
    void drawToTiles(TETile[][] tiles, TETile wall, TETile floor);
}
