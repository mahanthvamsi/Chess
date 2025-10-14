package model;


import java.util.*;
public class Board implements Cloneable{
private Piece[][] grid = new Piece[8][8];
public Board(){ }
public boolean inBounds(int r,int c){ return r>=0 && r<8 && c>=0 && c<8; }
public Piece getPiece(int r,int c){ return grid[r][c]; }
public Piece getPiece(Position p){ return getPiece(p.row,p.col); }
public void setPiece(int r,int c, Piece p){ grid[r][c]=p; if(p!=null) p.setPosition(new Position(r,c)); }
public void setPiece(Position p, Piece piece){ setPiece(p.row,p.col,piece); }
private GameController controller;
public void setController(GameController c){ this.controller = c; }
public GameController getGameController(){ return controller; }

public void initialize(){
// clear
    for(int r=0;r<8;r++) 
    for(int c=0;c<8;c++) grid[r][c]=null;
// Pawns
    for(int c=0;c<8;c++){ setPiece(6,c,new Pawn(Color.WHITE,new Position(6,c))); setPiece(1,c,new Pawn(Color.BLACK,new Position(1,c))); }
// Rooks
setPiece(7,0,new Rook(Color.WHITE,new Position(7,0))); setPiece(7,7,new Rook(Color.WHITE,new Position(7,7)));
setPiece(0,0,new Rook(Color.BLACK,new Position(0,0))); setPiece(0,7,new Rook(Color.BLACK,new Position(0,7)));
// Knights
setPiece(7,1,new Knight(Color.WHITE,new Position(7,1))); setPiece(7,6,new Knight(Color.WHITE,new Position(7,6)));
setPiece(0,1,new Knight(Color.BLACK,new Position(0,1))); setPiece(0,6,new Knight(Color.BLACK,new Position(0,6)));
// Bishops
setPiece(7,2,new Bishop(Color.WHITE,new Position(7,2))); setPiece(7,5,new Bishop(Color.WHITE,new Position(7,5)));
setPiece(0,2,new Bishop(Color.BLACK,new Position(0,2))); setPiece(0,5,new Bishop(Color.BLACK,new Position(0,5)));
// Queens
setPiece(7,3,new Queen(Color.WHITE,new Position(7,3)));
setPiece(0,3,new Queen(Color.BLACK,new Position(0,3)));
// Kings
setPiece(7,4,new King(Color.WHITE,new Position(7,4))); setPiece(0,4,new King(Color.BLACK,new Position(0,4)));
}
public void movePiece(Position from, Position to){ Piece p = getPiece(from); setPiece(to, p); setPiece(from, null); }
@Override public Board clone(){ try{ Board b = (Board)super.clone(); b.grid = new Piece[8][8]; for(int r=0;r<8;r++) for(int c=0;c<8;c++){ Piece p = this.grid[r][c]; if(p!=null) b.grid[r][c]=copyPiece(p); } return b; }catch(Exception e){ throw new RuntimeException(e);} }
private Piece copyPiece(Piece p){ if(p instanceof Pawn) return new Pawn(p.getColor(), p.getPosition()); if(p instanceof Rook) return new Rook(p.getColor(), p.getPosition()); if(p instanceof Knight) return new Knight(p.getColor(), p.getPosition()); if(p instanceof Bishop) return new Bishop(p.getColor(), p.getPosition()); if(p instanceof Queen) return new Queen(p.getColor(), p.getPosition()); if(p instanceof King){ King k=new King(p.getColor(), p.getPosition()); return k; } return null; }
public Position findKing(Color color){ for(int r=0;r<8;r++) for(int c=0;c<8;c++){ Piece p=grid[r][c]; if(p instanceof King && p.getColor()==color) return new Position(r,c); } return null; }
public List<Piece> piecesOfColor(Color color){ List<Piece> res=new ArrayList<>(); for(int r=0;r<8;r++) for(int c=0;c<8;c++){ Piece p=grid[r][c]; if(p!=null && p.getColor()==color) res.add(p);} return res; }
public void debugPrintPieces() {
    System.out.println("=== Board piece positions ===");
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            Piece p = getPiece(new Position(r, c));
            if (p != null) {
                System.out.println(p.getClass().getSimpleName() + " " + p.getColor() + " at " + new Position(r, c).toAlgebraic());
            }
        }
    }
}

}