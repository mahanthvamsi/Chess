package model;


import model.Board;
import model.Color;
import input.Position;
import java.util.*;
import controller.*;

public abstract class Piece implements Cloneable {
    protected Color color;
    protected Position pos;
    protected String repr;  

    public Piece(Color c, Position p, String r) {
        color = c;
        pos = p;
        repr = r;
    }

    public String getRepr() { return repr; }

    public Color getColor() { return color; }
    public Position getPosition() { return pos; }
    public void setPosition(Position p) { pos = p; }

    

    public abstract List<Position> legalMoves(Board b);

    @Override
    public Piece clone() {
        try { return (Piece) super.clone(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}

