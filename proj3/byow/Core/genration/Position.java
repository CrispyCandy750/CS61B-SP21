package byow.Core.genration;

public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position shift(int dx, int dy) {
        return new Position(x + dx, y + dy);
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
