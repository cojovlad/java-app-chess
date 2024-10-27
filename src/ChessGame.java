public class ChessGame {
    public String[][] gameBoard;
    public boolean isWhiteTurn;

    public ChessGame() {
        gameBoard = new String[8][8];
        isWhiteTurn = true;
        cleanBoard();
    }

    public void cleanBoard() {
        removeEveryPiece();
        initializeSecondRowPieces();
        initializeFirstRowPieces();
    }

    private void removeEveryPiece() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = "-";
            }
        }
    }

    private void initializeSecondRowPieces() {
        for (int i = 0; i < 8; i++) {
            gameBoard[1][i] = "♙";  // White Pawns
            gameBoard[6][i] = "♟";  // Black Pawns
        }
    }

    private void initializeFirstRowPieces() {
        // Place White pieces
        String[] whitePieces = {"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"};
        String[] blackPieces = {"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"};
        System.arraycopy(whitePieces, 0, gameBoard[0], 0, 8);
        System.arraycopy(blackPieces, 0, gameBoard[7], 0, 8);
    }

    public void printCurrentBoard() {
        System.out.println("    a   b   c   d   e   f   g   h");
        System.out.println("  ┌───┬───┬───┬───┬───┬───┬───┬───┐");
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " │");  // Row numbers on the left
            for (int j = 0; j < 8; j++) {
                String piece = gameBoard[i][j].equals("-") ? "   " : " " + gameBoard[i][j] + " ";
                System.out.print(piece + "│");
            }
            System.out.println(" " + (8 - i));  // Row numbers on the right

            if (i < 7) {
                System.out.println("  ├───┼───┼───┼───┼───┼───┼───┼───┤");
            } else {
                System.out.println("  └───┴───┴───┴───┴───┴───┴───┴───┘");
            }
        }
        System.out.println("    a   b   c   d   e   f   g   h");
        System.out.println((isWhiteTurn ? "White" : "Black") + "'s turn");
    }

    public boolean parseAndMove(String input) {
        String[] positions = input.trim().split(" ");
        if (positions.length != 2) {
            System.out.println("Invalid move format. Use: e2 e4");
            return false;
        }

        int[] start = chessNotationToIndex(positions[0]);
        int[] end = chessNotationToIndex(positions[1]);

        if (start == null || end == null) {
            System.out.println("Invalid chess notation.");
            return false;
        }

        // Print to debug
        System.out.printf("Moving piece from %s to %s\n", positions[0], positions[1]);

        return movePiece(start[0], start[1], end[0], end[1]);
    }

    private int[] chessNotationToIndex(String pos) {
        if (pos.length() != 2) return null;
        int col = pos.charAt(0) - 'a';  // 'a' -> 0, 'b' -> 1, etc.
        int row = 8 - (pos.charAt(1) - '0');  // '8' -> 0, '7' -> 1, etc.
        if (col < 0 || col > 7 || row < 0 || row > 7) return null;
        return new int[]{row, col};
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        String piece = gameBoard[startRow][startCol];
        if (piece.equals("-")) {
            System.out.println("No piece at starting position.");
            return false;
        }

        // Check if the piece belongs to the current player
        if ((isWhiteTurn && !piece.matches("[♙♖♘♗♕♔]")) ||
                (!isWhiteTurn && !piece.matches("[♟♜♞♝♛♚]"))) {
            System.out.println("It's " + (isWhiteTurn ? "White's" : "Black's") + " turn.");
            return false;
        }

        // Check if the target square contains a piece of the same color
        if (!gameBoard[endRow][endCol].equals("-") &&
                ((isWhiteTurn && Character.isUpperCase(gameBoard[endRow][endCol].charAt(0))) ||
                        (!isWhiteTurn && Character.isLowerCase(gameBoard[endRow][endCol].charAt(0))))) {
            System.out.println("Cannot capture your own piece.");
            return false;
        }

        // Debug: Print the piece and its potential move
        System.out.printf("Attempting to move %s from %s to %s\n", piece, chessIndexToNotation(startRow, startCol), chessIndexToNotation(endRow, endCol));

        boolean isValidMove = false;
        switch (piece) {
            case "♙":  // White Pawn
                isValidMove = isValidPawnMove(startRow, startCol, endRow, endCol, true);
                break;
            case "♟":  // Black Pawn
                isValidMove = isValidPawnMove(startRow, startCol, endRow, endCol, false);
                break;
            case "♖": case "♜":  // Rook
                isValidMove = isValidRookMove(startRow, startCol, endRow, endCol);
                break;
            case "♘": case "♞":  // Knight
                isValidMove = isValidKnightMove(startRow, startCol, endRow, endCol);
                break;
            case "♗": case "♝":  // Bishop
                isValidMove = isValidBishopMove(startRow, startCol, endRow, endCol);
                break;
            case "♕": case "♛":  // Queen
                isValidMove = isValidQueenMove(startRow, startCol, endRow, endCol);
                break;
            case "♔": case "♚":  // King
                isValidMove = isValidKingMove(startRow, startCol, endRow, endCol);
                break;
        }

        if (isValidMove) {
            // Perform the move
            gameBoard[endRow][endCol] = piece;
            gameBoard[startRow][startCol] = "-";
            System.out.printf("Moved %s from %s to %s\n", piece, chessIndexToNotation(startRow, startCol), chessIndexToNotation(endRow, endCol));
            switchTurn();
            return true;
        } else {
            System.out.println("Invalid move for " + piece);
            return false;
        }
    }

    private boolean isValidPawnMove(int startRow, int startCol, int endRow, int endCol, boolean isWhite) {
        int direction = isWhite ? -1 : 1;  // White pawns move up (-1), Black pawns move down (+1)

        // Move forward one square
        if (startCol == endCol && gameBoard[endRow][endCol].equals("-")) {
            if (endRow - startRow == direction) return true;

            // Move forward two squares from the starting position
            if ((isWhite && startRow == 6 || !isWhite && startRow == 1) && endRow - startRow == 2 * direction) {
                // Ensure path is clear for two-square move
                return gameBoard[startRow + direction][startCol].equals("-") && gameBoard[endRow][endCol].equals("-");
            }
        }
        // Capture diagonally
        else if (Math.abs(endCol - startCol) == 1 && endRow - startRow == direction) {
            if (!gameBoard[endRow][endCol].equals("-")) {
                boolean isOpponentPiece = isWhite != gameBoard[endRow][endCol].matches("[♙♖♘♗♕♔]");
                System.out.printf("Capturing piece? %b\n", isOpponentPiece);
                return isOpponentPiece;
            }
        }

        return false;
    }

    private boolean isValidRookMove(int startRow, int startCol, int endRow, int endCol) {
        if (startRow != endRow && startCol != endCol) return false;
        return isPathClear(startRow, startCol, endRow, endCol);
    }

    private boolean isValidKnightMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private boolean isValidBishopMove(int startRow, int startCol, int endRow, int endCol) {
        return Math.abs(startRow - endRow) == Math.abs(startCol - endCol) && isPathClear(startRow, startCol, endRow, endCol);
    }

    private boolean isValidQueenMove(int startRow, int startCol, int endRow, int endCol) {
        return isValidRookMove(startRow, startCol, endRow, endCol) || isValidBishopMove(startRow, startCol, endRow, endCol);
    }

    private boolean isValidKingMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        return rowDiff <= 1 && colDiff <= 1;
    }

    private boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {
        int rowDirection = Integer.signum(endRow - startRow);
        int colDirection = Integer.signum(endCol - startCol);

        int row = startRow + rowDirection;
        int col = startCol + colDirection;
        while (row != endRow || col != endCol) {
            if (!gameBoard[row][col].equals("-")) return false;
            row += rowDirection;
            col += colDirection;
        }
        return true;
    }

    private void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    private String chessIndexToNotation(int row, int col) {
        return String.valueOf((char) (col + 'a')) + (8 - row);
    }
}
