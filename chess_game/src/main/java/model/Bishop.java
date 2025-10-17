package model;

import model.Board;
import model.Color;
import input.Position;
import java.util.*;

public class Bishop extends Piece{
public Bishop(Color c, Position p) {
    super(c, p, c == Color.WHITE ? "\u2657" : "\u265D");
}
@Override public List<Position> legalMoves(Board b){
    return linearMoves(b, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
}
protected List<Position> linearMoves(Board b,int[][] dirs){
    List<Position> m=new ArrayList<>();
    for(int[] d:dirs){ int r=pos.row+d[0], c=pos.col+d[1]; while(b.inBounds(r,c)){
        Piece p=b.getPiece(r,c); if(p==null){ 
            m.add(new Position(r,c)); 
        } else { 
            if(p.getColor()!=color) m.add(new Position(r,c)); 
            break; 
        } r+=d[0]; c+=d[1]; 
    }
}
return m;
}
}