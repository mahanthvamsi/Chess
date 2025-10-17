package model;

import model.Board;
import model.Color;
import input.Position;
import java.util.*;
import controller.*;

public class Queen extends Piece{
public Queen(Color c, Position p) {
    super(c, p, c == Color.WHITE ? "\u2655" : "\u265B");
}
@Override public List<Position> legalMoves(Board b){
    List<Position> m=new ArrayList<>();
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
    for(int[] d:dirs){ int r=pos.row+d[0], c=pos.col+d[1]; while(b.inBounds(r,c)){
        Piece p=b.getPiece(r,c); 
        if(p==null) m.add(new Position(r,c)); 
        else { 
            if(p.getColor()!=color) m.add(new Position(r,c)); 
            break; 
        } r+=d[0]; c+=d[1]; 
    }
    }
return m;
}
}