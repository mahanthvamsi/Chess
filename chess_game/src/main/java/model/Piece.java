package model;


import model.Board;
import model.Color;
import model.Position;
import java.util.*;

public abstract class Piece implements Cloneable {
    protected Color color;
    protected Position pos;
    protected char repr;

    public Piece(Color c, Position p, char r) {
        color = c;
        pos = p;
        repr = r;
    }

    public Color getColor() { return color; }
    public Position getPosition() { return pos; }
    public void setPosition(Position p) { pos = p; }
    public char getRepr() { return repr; }

    public abstract List<Position> legalMoves(Board b);

    @Override
    public Piece clone() {
        try { return (Piece) super.clone(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
