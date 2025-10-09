package model;

import model.Board;
import model.Color;
import model.Position;
import java.util.*;

public class Pawn extends Piece{
public Pawn(Color c, Position p){ 
    super(c,p, c==Color.WHITE ? 'P' : 'p'); 
}
@Override public List<Position> legalMoves(Board b){
List<Position> moves=new ArrayList<>();
int dir = (color==Color.WHITE)? -1 : 1; // white moves up (towards row 0)
int r = pos.row + dir; int c = pos.col;
// forward one
if(b.inBounds(r,c) && b.getPiece(r,c)==null) moves.add(new Position(r,c));
// forward two from starting rank
int startRow = (color==Color.WHITE)? 6:1;
if(pos.row==startRow){ int r2 = pos.row + 2*dir; if(b.inBounds(r2,c) && b.getPiece(r2,c)==null && b.getPiece(r,c)==null) moves.add(new Position(r2,c)); }
// captures
for(int dc : new int[]{-1,1}){
    int cc = pos.col + dc; 
    if(b.inBounds(r,cc)){
        Piece p = b.getPiece(r,cc); 
        if(p!=null && p.getColor()!=color) 
            moves.add(new Position(r,cc));
    }
}
/// --- EN PASSANT CAPTURE ---
if (b.getGameController() != null) { // optional link; or pass via parameter if you modify method signature
    Position target = b.getGameController().getEnPassantTarget();
    if (target != null && target.row == pos.row + dir && Math.abs(target.col - pos.col) == 1) {
        moves.add(target);
    }
}
return moves;
}
}
