package game2048;

import java.util.Formatter;
import java.util.Iterator;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;


        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        board.setViewingPerspective(side);
        MoveResult moveResult = moveUp(board);
        board.setViewingPerspective(Side.NORTH);

        score += moveResult.score;
        changed = changed || moveResult.changed;

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /**
     * Execute move up operation.
     */
    private static MoveResult moveUp(Board board) {
        int totalScore = 0;
        boolean changed = false;
        for (int col = 0; col < board.size(); col++) {
            MoveResult moveResult = moveUpInCol(col, board);
            totalScore += moveResult.score;
            changed = changed || moveResult.changed;
        }
        return new MoveResult(totalScore, changed);
    }

    /**
     * Move up in col and returns score.
     */
    private static MoveResult moveUpInCol(int col, Board board) {
        int score = 0;
        boolean changed = false;

        for (int row = board.size() - 2, bound = board.size() - 1; row >= 0; row--) {

            Tile boundTile = board.tile(col, bound);
            Tile curTile = board.tile(col, row);
            if (curTile == null) {
                continue;
            } else if (boundTile == null) {
                board.move(col, bound, curTile);
                changed = true;
            } else if (curTile.value() == boundTile.value()) {
                board.move(col, bound, curTile);
                bound--;
                score += curTile.value() * 2;
                changed = true;
            } else { // curTile.value() != boundTile.value()
                bound--;
                board.move(col, bound, curTile);
                if (board.tile(col, row) == null) { // curTile is null, means changed
                    changed = true;
                }
            }
        }

        return new MoveResult(score, changed);
    }

    private static class MoveResult {
        int score;
        boolean changed;

        public MoveResult(int score, boolean changed) {
            this.score = score;
            this.changed = changed;
        }
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int size = b.size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size = b.size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Tile tile = b.tile(col, row);
                if (tile != null && tile.value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        return emptySpaceExists(b) || equivalentAdjacentTilesExist(b);
    }

    /**
     * Returns true if there are two adjacent tiles with the same value.
     */
    private static boolean equivalentAdjacentTilesExist(Board b) {
        int size = b.size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (equivalentAdjacentTileExists(b, col, row)) {
                    return true;
                }
            }
        }

        return false;
    }

    /** Returns true if there is equivalent adjacent tile of (col, row) tile */
    private static boolean equivalentAdjacentTileExists(Board b, int col, int row) {
        Tile tile = b.tile(col, row);
        if (tile == null) {
            return false;
        }

        Iterator<Tile> iterator = getNotNullAdjacentTiles(b, col, row);
        while (iterator.hasNext()) {
            Tile adjTile = iterator.next();
            if (adjTile.value() == tile.value()) {
                return true;
            }
        }

        return false;
    }

    /** Returns the not null adjacent tiles of (col, row) */
    private static Iterator<Tile> getNotNullAdjacentTiles(Board b, int col, int row) {
        return new AdjacentTilesIterator(b, col, row);
    }

    private static class AdjacentTilesIterator implements Iterator<Tile> {
        // up, right, down, left
        static final int[] DCOL = new int[]{0, 1, 0, -1};
        static final int[] DROW = new int[]{1, 0, -1, 0};
        Board b;
        int col, row, cur;

        AdjacentTilesIterator(Board b, int col, int row) {
            this.col = col;
            this.row = row;
            this.b = b;
            cur = 0;
            moveCur();
        }

        @Override
        public boolean hasNext() {
            return cur < DCOL.length;
        }

        @Override
        public Tile next() {
            Tile tile = b.tile(col + DCOL[cur], row + DROW[cur]);
            cur++;
            moveCur();
            return tile;
        }

        private void moveCur() {
            while (cur < DCOL.length && (!isValidSite(b, col + DCOL[cur], row + DROW[cur])
                    || b.tile(col + DCOL[cur], row + DROW[cur]) == null))
                cur++;
        }

        /** Returns true if the (col, row) is valid site i.e. not out of the bound. */
        private static boolean isValidSite(Board b, int col, int row) {
            int size = b.size();
            return col > 0 && row > 0 && col < size && row < size;
        }
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
