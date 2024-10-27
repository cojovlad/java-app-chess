import java.util.Scanner;

public class ChessCLI {
    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to CLI Chess!");
        chessGame.printCurrentBoard();

        while (true) {
            System.out.print((chessGame.isWhiteTurn() ? "White" : "Black") + " to move: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Game Over!");
                break;
            }

            if (chessGame.parseAndMove(input)) {
                chessGame.printCurrentBoard();
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
        scanner.close();
    }
}