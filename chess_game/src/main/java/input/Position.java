package input;

public class Position {
    public final int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position fromAlgebraic(String a) {
        if (a == null || a.length() != 2)
            throw new IllegalArgumentException("Invalid square: " + a);
        int col = a.charAt(0) - 'a';
        int row = 8 - (a.charAt(1) - '0');
        if (col < 0 || col > 7 || row < 0 || row > 7)
            throw new IllegalArgumentException("Invalid square: " + a);
        return new Position(row, col);
    }

    public String toAlgebraic() {
        return "" + (char) ('a' + col) + (8 - row);
    }
    public boolean inBounds() {
    return row >= 0 && row < 8 && col >= 0 && col < 8;
}


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position))
            return false;
        Position p = (Position) o;
        return p.row == row && p.col == col;
    }

    @Override
    public int hashCode() {
        return row * 31 + col;
    }
}
