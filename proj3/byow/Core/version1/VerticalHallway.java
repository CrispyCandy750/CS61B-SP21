package byow.Core.version1;

import byow.TileEngine.TETile;

public class VerticalHallway implements Hallway {
    public final Position bottomPoint;
    public final Position topPoint;

    public VerticalHallway(Position bottomPoint, Position topPoint) {
        this.bottomPoint = bottomPoint;
        this.topPoint = topPoint;
    }

    /** Draw this hallway in the 2D tile matrix. */
    @Override
    public void drawToTiles(TETile[][] tiles, TETile wall, TETile floor) {
        DeprecatedDrawUtils.drawVerticalLine(tiles, bottomPoint.shiftX(-1), topPoint.shiftX(-1), wall);
        DeprecatedDrawUtils.drawVerticalLine(tiles, bottomPoint, topPoint, floor);
        DeprecatedDrawUtils.drawVerticalLine(tiles, bottomPoint.shiftX(1), topPoint.shiftX(1), wall);
    }
}
