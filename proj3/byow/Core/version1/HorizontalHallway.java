package byow.Core.version1;

import byow.TileEngine.TETile;

public class HorizontalHallway implements Hallway {
    public final Position leftPoint;
    public final Position rightPoint;

    public HorizontalHallway(Position bottomPoint, Position topPoint) {
        this.leftPoint = bottomPoint;
        this.rightPoint = topPoint;
    }

    /** Draw this hallway in the 2D tile matrix. */
    @Override
    public void drawToTiles(TETile[][] tiles, TETile wall, TETile floor) {
        DeprecatedDrawUtils.drawHorizontalLine(tiles, leftPoint.shiftY(1), rightPoint.shiftY(1), wall);
        DeprecatedDrawUtils.drawHorizontalLine(tiles, leftPoint, rightPoint, floor);
        DeprecatedDrawUtils.drawHorizontalLine(tiles, leftPoint.shiftY(-1), rightPoint.shiftY(-1), wall);
    }
}
