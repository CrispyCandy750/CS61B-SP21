package byow.Core.version2;

import byow.Core.version1.Direction;

public class Position {

    /**
     * Returns true if the position.x in [leftBoundary, rightBoundary].
     * if the leftBoundary is null, return true if position.x in (-inf, rightBoundary]
     */
    public static boolean isWithinVertically(Position position, Integer bottomBoundary,
            Integer topBoundary
    ) {
        return isWithin(position.y, bottomBoundary, topBoundary);
    }

    /**
     * Returns true if the position.x in [leftBoundary, rightBoundary].
     * if the leftBoundary is null, return true if position.x in (-inf, rightBoundary]
     */
    public static boolean isWithinHorizontally(Position position, Integer leftBoundary,
            Integer rightBoundary
    ) {
        return isWithin(position.x, leftBoundary, rightBoundary);
    }

    /* ------------------------ private static variable & method ------------------------ */

    /**
     * Returns true if the `value` in [firstBoundary, secondBoundary]
     * if firstBoundary is null, return true if `actual in (-∞, secondBoundary)`
     */
    private static boolean isWithin(int value, Integer firstBoundary, Integer secondBoundary) {
        if (firstBoundary == null && secondBoundary == null) {
            return false;
        } else if (firstBoundary == null) {
            return value <= secondBoundary;
        } else if (secondBoundary == null) {
            return value >= firstBoundary;
        } else {
            return value >= firstBoundary && value <= secondBoundary;
        }
    }

    /* ------------------------ public instance variable & method ------------------------ */

    public final int x;
    public final int y;
    public final byow.Core.version1.Direction direction;

    public Position(int x, int y) {
        this(x, y, null);
    }

    public Position(int x, int y, byow.Core.version1.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Position shift(int dx, int dy, Direction direction) {
        return new Position(x + dx, y + dy, direction);
    }

    public Position shift(int dx, int dy) {
        return shift(dx, dy, null);
    }

    public Position shiftX(int dx) {
        return shift(dx, 0);
    }

    public Position shiftY(int dy) {
        return shift(0, dy);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Position)) {
            return false;
        }

        Position o = (Position) obj;
        return this.x == o.x && this.y == o.y;
    }
}
