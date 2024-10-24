public class ChessGame {
    public String[][] gameBoard;

    public ChessGame() {
        gameBoard = new String[8][8];
    }

    public void cleanBoard() {
        removeEveryPiece();
        initalizeSecondRowPieces();
        initalizeFirstRowPieces();
    }
    public void removeEveryPiece() {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                gameBoard[i][j] = "-";
            }
        }
    }
    public void initalizeSecondRowPieces() {
        for(int i=0;i<8;i++)
        {
            gameBoard[1][i] = "♙";  // White Pawns
            gameBoard[6][i] = "♟";
        }
    }
    public void initalizeFirstRowPieces() {
        // Place White pieces
        gameBoard[0][0] = "♖";
        gameBoard[0][1] = "♘";
        gameBoard[0][2] = "♗";
        gameBoard[0][3] = "♕";
        gameBoard[0][4] = "♔";
        gameBoard[0][5] = "♗";
        gameBoard[0][6] = "♘";
        gameBoard[0][7] = "♖";

        // Place Black pieces
        gameBoard[7][0] = "♜";
        gameBoard[7][1] = "♞";
        gameBoard[7][2] = "♝";
        gameBoard[7][3] = "♛";
        gameBoard[7][4] = "♚";
        gameBoard[7][5] = "♝";
        gameBoard[7][6] = "♞";
        gameBoard[7][7] = "♜";
    }

    public void prinCurrentBoard() {
        for (int i=0;i<8;i++)
        {
            for (int j=0;j<8;j++)
            {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println("\n");
        }
    }
}
