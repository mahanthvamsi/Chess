package model;

import java.util.*;
import controller.GameController;
import input.Position;

public class Board implements Cloneable {

    private Piece[][] grid = new Piece[8][8];
    private GameController controller;

    public Board() { }

    /* ---------- Basic Getters / Setters ---------- */

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public Piece getPiece(int r, int c) {
        return grid[r][c];
    }

    public Piece getPiece(Position p) {
        return getPiece(p.row, p.col);
    }

    public void setPiece(int r, int c, Piece p) {
        grid[r][c] = p;
        if (p != null) {
            // always update the piece’s internal position
            p.setPosition(new Position(r, c));
        }
    }

    public void setPiece(Position p, Piece piece) {
        setPiece(p.row, p.col, piece);
    }

    public void setController(GameController c) {
        this.controller = c;
    }

    public GameController getGameController() {
        return controller;
    }

    /* ---------- Initialization ---------- */

    public void initialize() {
        // Clear the board
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                grid[r][c] = null;

        // Pawns
        for (int c = 0; c < 8; c++) {
            setPiece(6, c, new Pawn(Color.WHITE, new Position(6, c)));
            setPiece(1, c, new Pawn(Color.BLACK, new Position(1, c)));
        }

        // Rooks
        setPiece(7, 0, new Rook(Color.WHITE, new Position(7, 0)));
        setPiece(7, 7, new Rook(Color.WHITE, new Position(7, 7)));
        setPiece(0, 0, new Rook(Color.BLACK, new Position(0, 0)));
        setPiece(0, 7, new Rook(Color.BLACK, new Position(0, 7)));

        // Knights
        setPiece(7, 1, new Knight(Color.WHITE, new Position(7, 1)));
        setPiece(7, 6, new Knight(Color.WHITE, new Position(7, 6)));
        setPiece(0, 1, new Knight(Color.BLACK, new Position(0, 1)));
        setPiece(0, 6, new Knight(Color.BLACK, new Position(0, 6)));

        // Bishops
        setPiece(7, 2, new Bishop(Color.WHITE, new Position(7, 2)));
        setPiece(7, 5, new Bishop(Color.WHITE, new Position(7, 5)));
        setPiece(0, 2, new Bishop(Color.BLACK, new Position(0, 2)));
        setPiece(0, 5, new Bishop(Color.BLACK, new Position(0, 5)));

        // Queens
        setPiece(7, 3, new Queen(Color.WHITE, new Position(7, 3)));
        setPiece(0, 3, new Queen(Color.BLACK, new Position(0, 3)));

        // Kings
        setPiece(7, 4, new King(Color.WHITE, new Position(7, 4)));
        setPiece(0, 4, new King(Color.BLACK, new Position(0, 4)));
    }

    /* ---------- Move Logic ---------- */

    public void movePiece(Position from, Position to) {
        Piece p = getPiece(from);
        setPiece(to, p);
        setPiece(from, null);
    }

    /* ---------- Cloning ---------- */

    @Override
    public Board clone() {
        try {
            Board b = (Board) super.clone();
            b.grid = new Piece[8][8];

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = this.grid[r][c];
                    if (p != null) {
                        Piece copy = copyPiece(p);
                        b.grid[r][c] = copy;
                        // 🟢 ensure the cloned piece’s internal position is correct
                        copy.setPosition(new Position(r, c));
                    }
                }
            }

            b.controller = null; // clone is independent
            return b;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /** Deep copy of a single piece (creates new Position too). */
    private Piece copyPiece(Piece p) {
        Position posCopy = new Position(p.getPosition().row, p.getPosition().col);

        if (p instanceof Pawn) return new Pawn(p.getColor(), posCopy);
        if (p instanceof Rook) return new Rook(p.getColor(), posCopy);
        if (p instanceof Knight) return new Knight(p.getColor(), posCopy);
        if (p instanceof Bishop) return new Bishop(p.getColor(), posCopy);
        if (p instanceof Queen) return new Queen(p.getColor(), posCopy);
        if (p instanceof King) {
            King k = new King(p.getColor(), posCopy);
            if (((King)p).hasMoved()) k.setMoved();
            return k;
        }
        return null;
    }



    /* ---------- Utility Queries ---------- */

    public Position findKing(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.getColor() == color) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }

    public List<Piece> piecesOfColor(Color color) {
        List<Piece> res = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == color) {
                    res.add(p);
                }
            }
        }
        return res;
    }

}

