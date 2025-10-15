package controller;

import java.util.*;
import model.*;
import input.Position;
import view.ConsoleUI;

public class GameController {
    private Board board;
    private ConsoleUI ui;
    private Color turn;
    private Scanner sc;
    private String whiteName = "White", blackName = "Black";
    private Position enPassantTarget = null;
    private List<String> moveLog = new ArrayList<>();
    private List<Piece> whiteCaptures = new ArrayList<>();
    private List<Piece> blackCaptures = new ArrayList<>();


    public Position getEnPassantTarget() { return enPassantTarget; }

    public GameController() {
        board = new Board();
        board.setController(this);
        board.initialize();
        ui = new ConsoleUI();
        turn = Color.WHITE;
        sc = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GameController g = new GameController();
        g.startGame();
    }

    private void showPossibleMoves() {
        ui.printMessage("Possible moves for " + (turn == Color.WHITE ? whiteName : blackName) + ":");
        for (Piece p : board.piecesOfColor(turn)) {
            var moves = p.legalMoves(board);
            if (!moves.isEmpty()) {
                System.out.print(p.getClass().getSimpleName() + " " + p.getPosition().toAlgebraic() + ": ");
                for (Position m : moves) System.out.print(m.toAlgebraic() + " ");
                System.out.println();
            }
        }
    }

    private void giveHint() {
        var allMoves = new ArrayList<String>();
        for (Piece p : board.piecesOfColor(turn)) {
            for (Position to : p.legalMoves(board)) {
                allMoves.add(p.getPosition().toAlgebraic() + to.toAlgebraic());
            }
        }
        if (allMoves.isEmpty()) {
            ui.printMessage("No valid moves!");
            return;
        }
        Random r = new Random();
        String hintMove = allMoves.get(r.nextInt(allMoves.size()));
        ui.printMessage("Try this move: " + hintMove);
    }

    public void startGame() {
        ui.printBoard(board);
        ui.printMessage("Welcome to Console Chess");
        ui.printMessage("Enter player names (or press enter for defaults):");

        System.out.print("White name: ");
        String w = sc.nextLine().trim();
        if (!w.isEmpty()) whiteName = w;

        System.out.print("Black name: ");
        String b = sc.nextLine().trim();
        if (!b.isEmpty()) blackName = b;

        ui.printBoard(board);

        while (true) {
            ui.printMessage((turn == Color.WHITE ? whiteName : blackName) + "'s move (e.g. e2e4). Type 'quit' to exit:");
            String cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("q")) {
                ui.printMessage("Quitting...");
                break;
            }
            if (cmd.equalsIgnoreCase("pip")) {
                showPossibleMoves();
                continue;
            }
            if (cmd.equalsIgnoreCase("hint")) {
                giveHint();
                continue;
            }

            try {
                boolean ok = processMove(cmd);
                if (ok) {
                    ui.printBoard(board);
                    showCaptures();
                    if (isKingCaptured(Color.BLACK)) {
                        ui.printMessage(whiteName + " wins by capturing the king!");
                        break;
                    }
                    if (isKingCaptured(Color.WHITE)) {
                        ui.printMessage(blackName + " wins by capturing the king!");
                        break;
                    }

                    if (isCheckmate(opponent(turn))) {
                        ui.printMessage((turn == Color.WHITE ? whiteName : blackName) + " wins by checkmate!");
                        showGameOverMenu((turn == Color.WHITE ? whiteName : blackName));
                        break;
                    }


                    if (isStalemate(opponent(turn))) {
                        ui.printMessage("Game drawn by stalemate!");
                        break;
                    }

                    switchTurn();
                }

            } catch (Exception e) {
                ui.printMessage("Error: " + e.getMessage());
            }
        }
    }

    private Color opponent(Color c) { return c == Color.WHITE ? Color.BLACK : Color.WHITE; }

    public boolean processMove(String input) {
        input = input.replaceAll("\\s+", "");
        if (input.length() != 4) throw new IllegalArgumentException("Use format e2e4");

        Position from = Position.fromAlgebraic(input.substring(0, 2));
        Position to = Position.fromAlgebraic(input.substring(2, 4));
        Piece p = board.getPiece(from);
        if (p == null) throw new IllegalArgumentException("No piece at " + from.toAlgebraic());
        if (p.getColor() != turn) throw new IllegalArgumentException("Not your piece");

        List<Position> legal = p.legalMoves(board);
        boolean found = false;
        for (Position pos : legal)
            if (pos.equals(to)) { found = true; break; }
        if (!found) throw new IllegalArgumentException("Wrong move. Check again.");

        Board copy = board.clone();
        copy.movePiece(from, to);
        if (wouldBeInCheck(copy, turn)) throw new IllegalArgumentException("Move would leave your king in check");

        Piece dest = board.getPiece(to);
        if (dest != null) {
            if (turn == Color.WHITE)
                whiteCaptures.add(dest);
            else
                blackCaptures.add(dest);
        }
        board.movePiece(from, to);


        Piece moved = board.getPiece(to);
        enPassantTarget = null;

        if (moved instanceof Pawn) {
            int diff = Math.abs(from.row - to.row);

            // En passant setup
            if (diff == 2) {
                int rowBetween = (from.row + to.row) / 2;
                enPassantTarget = new Position(rowBetween, from.col);
            }

            // En passant capture
            if (dest == null && from.col != to.col) {
                int captureRow = (moved.getColor() == Color.WHITE) ? to.row + 1 : to.row - 1;
                board.setPiece(new Position(captureRow, to.col), null);
            }

            // Pawn promotion
            if ((moved.getColor() == Color.WHITE && to.row == 0) ||
                (moved.getColor() == Color.BLACK && to.row == 7)) {

                ui.printMessage("Pawn promotion! Choose (Q,R,B,N):");
                String choice = sc.nextLine().trim();
                char ch = choice.isEmpty() ? 'Q' : Character.toUpperCase(choice.charAt(0));

                Piece promo = switch (ch) {
                    case 'R' -> new Rook(moved.getColor(), to);
                    case 'B' -> new Bishop(moved.getColor(), to);
                    case 'N' -> new Knight(moved.getColor(), to);
                    default -> new Queen(moved.getColor(), to);
                };

                board.setPiece(to, promo);
            }
        }

        if (moved instanceof King) ((King) moved).setMoved();
        String player = (turn == Color.WHITE ? whiteName : blackName);
        moveLog.add(player + ": " + input.toLowerCase());
        return true;
    }

    private boolean isKingCaptured(Color color) { return board.findKing(color) == null; }
    private void switchTurn() { turn = opponent(turn); }

    private boolean wouldBeInCheck(Board b, Color color) {
        Position kingPos = b.findKing(color);
        if (kingPos == null) return true;

        Color opp = opponent(color);
        for (Piece op : b.piecesOfColor(opp)) {
            if (op instanceof Pawn) {
                for (Position atk : ((Pawn) op).getAttackSquares(b)) {
                    if (atk.equals(kingPos)) return true;
                }
            } else {
                List<Position> attacks = op.legalMoves(b);
                for (Position p : attacks)
                    if (p.equals(kingPos)) return true;
            }
        }
        return false;
    }

    public boolean isCheckmate(Color defending) {
        if (!wouldBeInCheck(board, defending)) return false;

        for (Piece pc : board.piecesOfColor(defending)) {
            Position from = pc.getPosition();
            for (Position to : pc.legalMoves(board)) {
                Board copy = board.clone();
                copy.movePiece(from, to);
                if (!wouldBeInCheck(copy, defending)) return false;
            }
        }
        return true;
    }

    public boolean isStalemate(Color defending) {
        if (wouldBeInCheck(board, defending)) return false;

        for (Piece pc : board.piecesOfColor(defending)) {
            Position from = pc.getPosition();
            for (Position to : pc.legalMoves(board)) {
                Board copy = board.clone();
                copy.movePiece(from, to);
                if (!wouldBeInCheck(copy, defending)) return false;
            }
        }
        return true;
    }
    private void showGameOverMenu(String winner) {
        ui.printMessage("\nGame Over! Winner: " + winner);
        ui.printMessage("1. View move log");
        ui.printMessage("2. Exit");
        System.out.print("Enter your choice: ");
        
        String choice = sc.nextLine().trim();
        if (choice.equals("1")) {
            printMoveLog();
            System.out.println("\nPress Enter to exit...");
            sc.nextLine();
        }

        ui.printMessage("Thanks for playing!");
        System.exit(0);
    }
    private void printMoveLog() {
        ui.printMessage("\n===== Move Log =====");
        int moveNumber = 1;
        for (String move : moveLog) {
            System.out.printf("%2d. %s%n", moveNumber++, move);
        }
    }
    private void showCaptures() {
        System.out.println("\n📜 Captured Pieces:");
        System.out.print("White has captured: ");
        for (Piece p : whiteCaptures)
            System.out.print(p.getRepr() + " ");
        System.out.println();

        System.out.print("Black has captured: ");
        for (Piece p : blackCaptures)
            System.out.print(p.getRepr() + " ");
        System.out.println("\n");
    }


}
