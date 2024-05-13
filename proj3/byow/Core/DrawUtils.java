package byow.Core;

import byow.Core.genration.Hallway;
import byow.Core.genration.Position;
import byow.Core.genration.Room;
import byow.Core.genration.TurnDirection;
import byow.TileEngine.TETile;

/** The utils that draw something in the 2D tile matrix. */
public class DrawUtils {

    /** Draw a room in the tiles */
    public static void drawRoom(TETile[][] tiles, Room room, TETile wall, TETile floor) {
        Position bottomLeftPoint = room.getPosition();
        int width = room.getWidth();
        int height = room.getHeight();

        /* Draw the bottom wall. */
        drawHorizontalLine(tiles, bottomLeftPoint, width, wall);
        /* Draw the top wall. */
        drawHorizontalLine(tiles, bottomLeftPoint.shiftY(height - 1), width, wall);
        /* Draw the left wall. */
        drawVerticalLine(tiles, bottomLeftPoint, height, wall);
        /* Draw the right wall. */
        drawVerticalLine(tiles, bottomLeftPoint.shiftX(width - 1), height, wall);

        /* Draw the floor. */
        for (int dy = 1, floorWidth = width - 2; dy < height - 1; dy++) {
            drawHorizontalLine(tiles, bottomLeftPoint.shift(1, dy), floorWidth, floor);
        }
    }

    /** Draw a hallway in the tiles */
    public static void drawHallway(TETile[][] tiles, Hallway hallway, TETile wall, TETile floor) {
        hallway.drawToTiles(tiles, wall, floor);
    }

    /** Draw a vertical line in the 2D tile matrix at given position with given tile. */
    public static void drawHorizontalLine(TETile[][] tiles, Position leftPoint, Position rightPoint,
            TETile tile
    ) {
        drawHorizontalLine(tiles, leftPoint, rightPoint.x - leftPoint.x + 1, tile);
    }

    /** Draw a horizontal line in the 2D tile matrix at given position with given tile. */
    public static void drawVerticalLine(TETile[][] tiles, Position bottomPoint, Position topPoint,
            TETile tile
    ) {
        drawVerticalLine(tiles, bottomPoint, topPoint.y - bottomPoint.y + 1, tile);
    }

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


    /** Draw a turning line in the 2D tile matrix. */
    public static void drawTurningLine(TETile[][] tiles, Position leftPoint, Position turningPoint, Position rightPoint,
            TurnDirection turnDirection, TETile tile
    ) {
        switch (turnDirection) {
            case RIGHT_UP:
                drawHorizontalLine(tiles, leftPoint, turningPoint, tile);
                drawVerticalLine(tiles, turningPoint, rightPoint, tile);
                break;
            case RIGHT_DOWN:
                drawHorizontalLine(tiles, leftPoint, turningPoint, tile);
                drawVerticalLine(tiles, rightPoint, turningPoint, tile);
                break;
            case UP_RIGHT:
                drawVerticalLine(tiles, leftPoint, turningPoint, tile);
                drawHorizontalLine(tiles, turningPoint, rightPoint, tile);
                break;
            case DOWN_RIGHT:
                drawVerticalLine(tiles, turningPoint, leftPoint, tile);
                drawHorizontalLine(tiles, turningPoint, rightPoint, tile);
        }
    }

    /** Draw a turning line in the 2D tile matrix. */
    public static void drawTurningLine(TETile[][] tiles, Position leftPoint, Position rightPoint,
            TurnDirection turnDirection, TETile tile
    ) {
        Position turningPoint = TurnDirection.getTurningPoint(leftPoint, rightPoint, turnDirection);
        drawTurningLine(tiles, leftPoint, turningPoint, rightPoint, turnDirection, tile);
    }
}
