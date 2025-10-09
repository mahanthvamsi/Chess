package model;

import model.Board;
import model.Color;
import model.Position;
import java.util.*;

public class Knight extends Piece{
public Knight(Color c, Position p){ super(c,p, c==Color.WHITE ? 'N':'n'); }
@Override public List<Position> legalMoves(Board b){
List<Position> m=new ArrayList<>(); int[][] deltas={{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
for(int[] d: deltas){ int r=pos.row+d[0], c=pos.col+d[1]; if(b.inBounds(r,c)){ Piece p=b.getPiece(r,c); if(p==null || p.getColor()!=color) m.add(new Position(r,c)); }}
return m;
}
}