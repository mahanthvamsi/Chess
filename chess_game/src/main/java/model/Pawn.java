package model;

import model.Board;
import model.Color;
import input.Position;
import java.util.*;

public class Pawn extends Piece {

    public Pawn(Color c, Position p) {
    super(c, p, c == Color.WHITE ? "\u2659" : "\u265F");
}

@Override
public List<Position> legalMoves(Board b) {
    List<Position> moves = new ArrayList<>();
    int dir = (color == Color.WHITE) ? -1 : 1; // white moves up
    int r = pos.row + dir;
    int c = pos.col;

    if (b.inBounds(r, c) && b.getPiece(r, c) == null) {
        moves.add(new Position(r, c));

        int startRow = (color == Color.WHITE) ? 6 : 1;
        int r2 = pos.row + 2 * dir;
        if (pos.row == startRow && b.inBounds(r2, c)
                && b.getPiece(r2, c) == null && b.getPiece(r, c) == null) {
            moves.add(new Position(r2, c));
        }
    }

    // --- captures ---
    for (int dc : new int[]{-1, 1}) {
        int cc = pos.col + dc;
        if (b.inBounds(r, cc)) {
            Piece p = b.getPiece(r, cc);
            if (p != null && p.getColor() != color) {
                moves.add(new Position(r, cc));
            }
        }
    }

    // --- En Passant ---
    if (b.getGameController() != null) {
        Position target = b.getGameController().getEnPassantTarget();
        if (target != null && target.row == pos.row + dir
                && Math.abs(target.col - pos.col) == 1) {
            moves.add(target);
        }
    }

    return moves;
}


    public List<Position> getAttackSquares(Board b) {
        List<Position> attacks = new ArrayList<>();
        int dir = (color == Color.WHITE) ? -1 : 1;
        int r = pos.row + dir;

        for (int dc : new int[]{-1, 1}) {
            int c = pos.col + dc;
            if (b.inBounds(r, c)) {
                attacks.add(new Position(r, c));
            }
        }
        return attacks;
    }
}
