package model;


import model.Board;
import model.Color;
import input.Position;
import java.util.*;

public class King extends Piece {
    private boolean hasMoved = false;

    public King(Color c, Position p) {
        super(c, p, c == Color.WHITE ? "\u2654" : "\u265A");
    }


    public void setMoved() { hasMoved = true; }
    public boolean hasMoved() { return hasMoved; }

    @Override
    public List<Position> legalMoves(Board b) {
        List<Position> moves = new ArrayList<>();

        // --- Normal one-square moves ---
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int r = pos.row + dr, c = pos.col + dc;
                if (b.inBounds(r, c)) {
                    Piece p = b.getPiece(r, c);
                    if (p == null || p.getColor() != color) {
                        moves.add(new Position(r, c));
                    }
                }
            }
        }

        // --- Simplified castling (no check conditions) ---
        if (!hasMoved) {
            int row = pos.row;

            // King-side
            Piece rookK = b.getPiece(row, 7);
            if (rookK instanceof Rook) {
                boolean clear = true;
                for (int c = pos.col + 1; c < 7; c++) {
                    if (b.getPiece(row, c) != null) {
                        clear = false;
                        break;
                    }
                }
                if (clear) {
                    moves.add(new Position(row, pos.col + 2));
                }
            }

            // Queen-side
            Piece rookQ = b.getPiece(row, 0);
            if (rookQ instanceof Rook) {
                boolean clear = true;
                for (int c = 1; c < pos.col; c++) {
                    if (b.getPiece(row, c) != null) {
                        clear = false;
                        break;
                    }
                }
                if (clear) {
                    moves.add(new Position(row, pos.col - 2));
                }
            }
        }

        return moves;
    }
}
