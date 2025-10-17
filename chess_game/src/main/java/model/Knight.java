package model;

import model.Board;
import model.Color;
import input.Position;
import java.util.*;

public class Knight extends Piece {
    public Knight(Color c, Position p) {
        super(c, p, c == Color.WHITE ? "\u2658" : "\u265E");
    }

    @Override
    public List<Position> legalMoves(Board b) {
        List<Position> moves = new ArrayList<>();
        int[][] offsets = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] o : offsets) {
            int r = pos.row + o[0];
            int c = pos.col + o[1];
            if (b.inBounds(r, c)) {
                Piece target = b.getPiece(r, c);
                if (target == null || target.getColor() != color) {
                    moves.add(new Position(r, c));
                }
            }
        }

        return moves;
    }
}
