package byow.Core.genration;

import byow.Core.DrawUtils;
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
        DrawUtils.drawHorizontalLine(tiles, leftPoint.shiftY(1), rightPoint.shiftY(1), wall);
        DrawUtils.drawHorizontalLine(tiles, leftPoint, rightPoint, floor);
        DrawUtils.drawHorizontalLine(tiles, leftPoint.shiftY(-1), rightPoint.shiftY(-1), wall);
    }
}
