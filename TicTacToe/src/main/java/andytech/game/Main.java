package andytech.game;

import andytech.ai.AI;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Author: Andres Toledo
 *
 * @author amtam
 * @version $Id: $Id
 */
public class Main {

    //Used for user input
    /**
     * Constant <code>scanner</code>
     */
    public static Scanner scanner;
    //AI player class
    /**
     * Constant <code>ai</code>
     */
    public static AI ai;

    /**
     * Application starts by calling main menu
     *
     * @param args Application input arguments if any.
     */
    public static void main(String[] args) {
        mainMenu();
    }

    /**
     * Main menu for the application.
     */
    public static void mainMenu() {
        //Initialize AI
        ai = new AI();
        //Loads previously trained AI from file.
        ai.loadBrain();
        //Requests user input for main menu
        scanner = new Scanner(System.in);
        int menu = 102;
        do {
            if (menu == 100) {
                //Manually initialize training
                ai.initTrainingAndTesting();
            } else if (menu == 101) {
                //Save brain manually to use on next execution. Saves file as tictac.brain.
                ai.saveBrain();
            } else if (menu == 102) {
                //Tests AI and displays current win/draw percentage
                ai.printCurrentPerformance();
            } else {
                //Starts a new game with AI. Human player moves first
                playGameBrain();
                System.out.println("Would you like to play again?");
            }
            //Repeats menu until 1000 is entered
            menu = scanner.nextInt();
        }
        while (menu != 1000);
    }

    /**
     * Plays game with AI.
     */
    public static void playGameBrain() {
        //Create new game board.
        Board board = new Board();
        //Set x as human player and move first.
        String humanPlayer = "x";
        board.setCurrentTurn(humanPlayer);
        String result = "";
        //While no winner the game will keep executing.
        while ((result = board.validateWinner()).length() == 0) {
            //If AI turn else human turn.
            if (!board.getCurrentTurn().equals(humanPlayer)) {
                //AI possible moves
                ArrayList<Integer> moves = ai.predictBrain(board.getLineBoard());
                int choice;
                //If more then one move is returned choose randomly
                boolean isValid;
                if (moves != null && !moves.isEmpty()) {
                    choice = new Random().nextInt(moves.size());
                    isValid = board.makeMove(board.getCurrentTurn(), moves.get(choice));
                    moves.remove(choice);
                } else {
                    //If no moves are returned choose randomly
                    isValid = board.makeMove(board.getCurrentTurn(), new Random().nextInt(9));
                }
                //Make sure move is valid if previous move invalid.
                while (!isValid) {
                    if (moves != null && !moves.isEmpty()) {
                        choice = new Random().nextInt(moves.size());
                        isValid = board.makeMove(board.getCurrentTurn(), moves.get(choice).intValue());
                        moves.remove(choice);
                    } else {
                        //If no moves are returned choose randomly
                        isValid = board.makeMove(board.getCurrentTurn(), new Random().nextInt(9));
                    }
                }
            } else {
                //Ask player for a move
                System.out.println("Make your move");
                int input = scanner.nextInt();
                input -= 1;
                //Make sure move is valid if input move invalid.
                while (!board.makeMove(board.getCurrentTurn(), input)) {
                    System.out.println("Invalid move make a new one");
                    input = scanner.nextInt();
                    input -= 1;
                }
            }
            //Print board after moves
            board.printBoard();
        }
        //Human won.
        if (result.equals("x")) {
            System.out.println("X won");
            //Train Loss.
            ai.trainBrainLoss(board.getMoveXHistory(), board.getBoardHistory());
            if (ai.getTrainingCounter() < 999999999) {
                ai.setTrainingCounter(ai.getTrainingCounter() + 1);
            } else {
                ai.setTestLossCounter(0.0);
                ai.setOverlap(ai.getOverlap() + 1);
            }
            //AI won.
        } else if (result.equals("o")) {
            System.out.println("O won");
            //Train Win.
            ai.trainBrain(board.getMoveOHistory(), board.getBoardHistory(), 0);
            if (ai.getTrainingCounter() < 999999999) {
                ai.setTrainingCounter(ai.getTrainingCounter() + 1);
            } else {
                ai.setTestLossCounter(0.0);
                ai.setOverlap(ai.getOverlap() + 1);
            }
            //If draw
        } else if (result.equals("d")) {
            //Train Draw.
            ai.trainBrain(board.getMoveOHistory(), board.getBoardHistory(), 0);
            if (ai.getTrainingCounter() < 999999999) {
                ai.setTrainingCounter(ai.getTrainingCounter() + 1);
            } else {
                ai.setTestLossCounter(0.0);
                ai.setOverlap(ai.getOverlap() + 1);
            }
            System.out.println("It's a draw");
        }
        //Print final board
        board.printBoard();
    }

}
