package byow.Core.version1;

import byow.TileEngine.TETile;

/** A Hallway contains turn. */
public class TurningHallway implements Hallway {

    /** The left point, e.g. bottom-left point in the right-up turning corner. */
    public Position leftPoint;
    /** Similar to `leftPoint`. */
    public Position rightPoint;
    /** The turning of a corner. */
    public TurnDirection turnDirection;

    public TurningHallway(Position leftPoint, Position rightPoint, TurnDirection turnDirection) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
        this.turnDirection = turnDirection;
    }

    @Override
    public void drawToTiles(TETile[][] tiles, TETile wall, TETile floor) {
        Position turningPoint = TurnDirection.getTurningPoint(leftPoint, rightPoint, turnDirection);

        /* Draw the floor. */
        DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint, turningPoint, rightPoint, turnDirection, floor);

        /* Draw the wall: each case draw the outer border, middle floor and inner border, respectively. */
        switch (turnDirection) {
            case RIGHT_UP:
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftY(1), turningPoint.shift(-1, 1),
                        rightPoint.shiftX(-1), turnDirection, wall);
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftY(-1), turningPoint.shift(1, -1),
                        rightPoint.shiftX(1), turnDirection, wall);
                break;
            case RIGHT_DOWN:
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftY(1), turningPoint.shift(1, 1),
                        rightPoint.shiftX(1), turnDirection, wall);

                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftY(-1), turningPoint.shift(-1, -1),
                        rightPoint.shiftX(-1), turnDirection, wall);
                break;
            case DOWN_RIGHT:
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftX(-1), turningPoint.shift(-1, -1),
                        rightPoint.shiftY(-1), turnDirection, wall);
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftX(-1), turningPoint.shift(1, 1),
                        rightPoint.shiftY(1), turnDirection, wall);
                break;
            case UP_RIGHT:
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftX(-1), turningPoint.shift(-1, 1),
                        rightPoint.shiftY(1), turnDirection, wall);
                DeprecatedDrawUtils.drawTurningLine(tiles, leftPoint.shiftX(1), turningPoint.shift(1, -1),
                        rightPoint.shiftY(-1), turnDirection, wall);
                break;
        }
    }
}
