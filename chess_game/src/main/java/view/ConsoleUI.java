package view;

import model.*;

public class ConsoleUI {

    private static final String RESET = "\u001B[0m";
    private static final String WHITE_PIECE = "\u001B[97m";   // bright white
    private static final String BLACK_PIECE = "\u001B[30m";   // real black
    private static final String BOARD_WHITE = "\u001B[48;5;230m";  // beige
    private static final String BOARD_BLACK = "\u001B[48;5;94m";   // brownish


    public void printBoard(Board b) {
        System.out.println("   a  b  c  d  e  f  g  h");
        for (int r = 0; r < 8; r++) {
            System.out.print((8 - r) + " ");
            for (int c = 0; c < 8; c++) {
                boolean isLight = (r + c) % 2 == 0;
                String bg = isLight ? BOARD_WHITE : BOARD_BLACK;

                Piece p = b.getPiece(r, c);
                if (p == null) {
                    System.out.print(bg + "   " + RESET);
                } else {
                    String color = (p.getColor() == Color.WHITE) ? WHITE_PIECE : BLACK_PIECE;
                    System.out.print(bg + " " + color + p.getRepr() + RESET + bg + " " + RESET);
                }
            }
            System.out.println(" " + (8 - r));
        }
        System.out.println("   a  b  c  d  e  f  g  h");
    }

    public void printMessage(String m) {
        System.out.println(m);
    }
}
