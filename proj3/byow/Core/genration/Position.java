package byow.Core.genration;

public class Position {

    /** Returns true if the position.x > left */
    public static boolean isToTheRightOf(Position position, int left) {
        return isWithinHorizontally(position, left + 1, null);
    }

    /** Returns true if the position.x < right */
    public static boolean isToTheLeftOf(Position position, int right) {
        return isWithinHorizontally(position, null, right - 1);
    }

    /** Returns true if the position.y < top */
    public static boolean isUnder(Position position, int top) {
        return isWithinVertically(position, null, top - 1);
    }

    /** Returns true if the position.y > bottom */
    public static boolean isAbove(Position position, int bottom) {
        return isWithinVertically(position, bottom + 1, null);
    }

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
     * Returns true if the `actual` in [firstBoundary, secondBoundary]
     * if firstBoundary is null, return true if `actual in (-∞, secondBoundary)`
     */
    public static boolean isWithin(int actual, Integer firstBoundary, Integer secondBoundary) {
        if (firstBoundary == null && secondBoundary == null) {
            return false;
        } else if (firstBoundary == null) {
            return actual <= secondBoundary;
        } else if (secondBoundary == null) {
            return actual >= firstBoundary;
        } else {
            return actual > firstBoundary && actual < secondBoundary;
        }
    }

    /* ------------------------ public instance variable & method ------------------------ */

    public final int x;
    public final int y;
    public final Direction direction;

    public Position(int x, int y) {
        this(x, y, null);
    }

    public Position(int x, int y, Direction direction) {
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
}
