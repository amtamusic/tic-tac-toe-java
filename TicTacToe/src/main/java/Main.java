import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Scanner scanner;
    public static AI ai;

    public static void main(String[] args) {
        ai = new AI();
        ai.loadBrain();
        scanner = new Scanner(System.in);
        int menu = 1;
        do {
            if (menu == 100) {
                ai.initTrainingAndTesting();
            }
            if (menu == 101) {
                ai.saveBrain();
            }
            playGameBrain();
            System.out.println("Would you like to play again?");
            menu = scanner.nextInt();
        }
        while (menu != 1000);
    }

    public static void playGameBrain() {
        Board board = new Board();
        String humanPlayer = "x";
        board.setCurrentTurn(humanPlayer);

        String result = "";
        while ((result = board.validateWinner()).length() == 0) {
            if (!board.getCurrentTurn().equals(humanPlayer)) {
                ArrayList<Integer> moves = ai.predictBrain(board.getLineBoard());
                int choice;
                boolean isValid;
                if (moves != null && !moves.isEmpty()) {
                    choice = new Random().nextInt(moves.size());
                    isValid = board.makeMove(board.currentTurn, moves.get(choice));
                    moves.remove(choice);
                } else {
                    isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                }
                while (!isValid) {
                    if (moves != null && !moves.isEmpty()) {
                        choice = new Random().nextInt(moves.size());
                        isValid = board.makeMove(board.currentTurn, moves.get(choice).intValue());
                        moves.remove(choice);
                    } else {
                        isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                    }
                }
            } else {
                System.out.println("Make your move");
                int input = scanner.nextInt();
                input -= 1;
                while (!board.makeMove(board.currentTurn, input)) {
                    System.out.println("Invalid move make a new one");
                    input = scanner.nextInt();
                    input -= 1;
                }
            }
            board.printBoard();
        }
        if (result.equals("x")) {
            System.out.println("X won");
            ai.trainBrainLoss(board.getMoveXHistory(), board.boardHistory);
            if (ai.trainingCounter < 999999999) {
                ai.trainingCounter++;
            } else {
                ai.trainingCounter = 0;
                ai.overlap++;
            }

        } else if (result.equals("o")) {
            System.out.println("O won");
            System.out.println(board.getMoveOHistory());
            ai.trainBrain(board.getMoveOHistory(), board.boardHistory, 0);
            if (ai.trainingCounter < 999999999) {
                ai.trainingCounter++;
            } else {
                ai.trainingCounter = 0;
                ai.overlap++;
            }

        } else if (result.equals("d")) {
            ai.trainBrain(board.getMoveOHistory(), board.boardHistory, 0);
            if (ai.trainingCounter < 999999999) {
                ai.trainingCounter++;
            } else {
                ai.trainingCounter = 0;
                ai.overlap++;
            }
            System.out.println("It's a draw");
        }
        board.printBoard();
    }

}