package model;


import model.Board;
import model.Color;
import model.Position;
import java.util.*;

public class King extends Piece{
private boolean hasMoved=false;
public King(Color c, Position p){ super(c,p, c==Color.WHITE ? 'K':'k'); }
public void setMoved(){ hasMoved=true; }
public boolean hasMoved(){ return hasMoved; }
@Override public List<Position> legalMoves(Board b){
List<Position> m=new ArrayList<>();
for(int dr=-1; dr<=1; dr++) for(int dc=-1; dc<=1; dc++){
if(dr==0 && dc==0) continue; int r=pos.row+dr, c=pos.col+dc; if(b.inBounds(r,c)){ Piece p=b.getPiece(r,c); if(p==null || p.getColor()!=color) m.add(new Position(r,c)); }
}

if(!hasMoved){ int row=pos.row; // rook at col 7?
Piece rook = b.getPiece(row,7); if(rook instanceof Rook){ // assume rook hasn't moved: not tracked for rook currently
// check clear between
boolean clear = true; for(int c=pos.col+1;c<7;c++) if(b.getPiece(row,c)!=null) clear=false;
if(clear) m.add(new Position(row,pos.col+2));
}
// Queen-side
Piece rookq = b.getPiece(row,0); if(rookq instanceof Rook){ boolean clear=true; for(int c=1;c<pos.col;c++) if(b.getPiece(row,c)!=null) clear=false; if(clear) m.add(new Position(row,pos.col-2)); }
}
return m;
}
}