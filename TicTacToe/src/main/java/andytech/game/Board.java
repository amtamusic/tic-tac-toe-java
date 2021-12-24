package andytech.game;

import lombok.Data;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author: Andres Toledo
 * Holds all things related to a tic-tac-toe game board.
 *
 * @author amtam
 * @version $Id: $Id
 */
@Data
public class Board {

    /**
     * Represents game board.
     */
    ArrayList<Space> board;
    /**
     * Used to track players in the game.
     */
    ArrayList<Player> players;
    /**
     * Used to track whose turn it is.
     */
    String currentTurn;
    /**
     * Used to track how many turns have passed.
     */
    int turnCount;
    /**
     * History of boards after each move.
     */
    ArrayList<Board> boardHistory;
    /**
     * History of moves player X made.
     */
    ArrayList<Integer> moveXHistory;
    /**
     * History of moves player Y made.
     */
    ArrayList<Integer> moveOHistory;

    /**
     * Initializes the game board,players and class variables.
     */
    public Board() {
        this.board = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            board.add(new Space("-"));
        }
        players = new ArrayList<>();
        players.add(new Player("x"));
        players.add(new Player("o"));
        currentTurn = players.get(new Random().nextInt(players.size())).name;
        boardHistory = new ArrayList<>();
        moveXHistory = new ArrayList<>();
        moveOHistory = new ArrayList<>();
    }

    /**
     * Changes turn from one player to another.
     */
    public void nextTurn() {
        if (currentTurn.equalsIgnoreCase("x")) {
            currentTurn = "o";
        } else {
            currentTurn = "x";
        }
        turnCount++;
    }

    /**
     * Determines weather a move is valid or not.
     *
     * @param position represents position on board where player wants to place a piece this value ranges from 0-8 inclusive.
     * @return If move is valid.
     */
    public boolean validMove(int position) {
        return board.get(position).getValue().equals("-");
    }

    /**
     * Updates board with valid move. Also adds previous board to board history and changes turn by calling {@link andytech.game.Board#nextTurn()}.
     *
     * @param value    Player value to put on board: x or o.
     * @param position Position chosen by player.
     * @return If player made move successfully.
     */
    public boolean makeMove(String value, int position) {
        if (validMove(position)) {
            if (moveXHistory.isEmpty() && moveOHistory.isEmpty()) {
                boardHistory.add(copyBoard());
            }
            board.set(position, new Space(value));
            boardHistory.add(copyBoard());
            if (value.equals("x")) {
                moveXHistory.add(position);
                moveOHistory.add(-1);
            }
            if (value.equals("o")) {
                moveOHistory.add(position);
                moveXHistory.add(-1);
            }
            nextTurn();
            return true;
        }
        return false;
    }

    /**
     * Makes a copy of the current board.
     *
     * @return Copy of the current board.
     */
    public Board copyBoard() {
        Board temp = new Board();
        for (int i = 0; i < this.board.size(); i++) {
            temp.getBoard().get(i).setValue(this.getBoard().get(i).getValue());
        }
        return temp;
    }

    /**
     * Validates win conditions.
     *
     * @return Game winner as string.
     */
    public String validateWinner() {
        String b = this.getLineBoard();
        //validate x
        if (b.charAt(0) == 'x' && b.charAt(1) == 'x' && b.charAt(2) == 'x') {
            return "x";
        }
        if (b.charAt(3) == 'x' && b.charAt(4) == 'x' && b.charAt(5) == 'x') {
            return "x";
        }
        if (b.charAt(6) == 'x' && b.charAt(7) == 'x' && b.charAt(8) == 'x') {
            return "x";
        }

        if (b.charAt(0) == 'x' && b.charAt(3) == 'x' && b.charAt(6) == 'x') {
            return "x";
        }
        if (b.charAt(1) == 'x' && b.charAt(4) == 'x' && b.charAt(7) == 'x') {
            return "x";
        }
        if (b.charAt(2) == 'x' && b.charAt(5) == 'x' && b.charAt(8) == 'x') {
            return "x";
        }

        if (b.charAt(0) == 'x' && b.charAt(4) == 'x' && b.charAt(8) == 'x') {
            return "x";
        }
        if (b.charAt(2) == 'x' && b.charAt(4) == 'x' && b.charAt(6) == 'x') {
            return "x";
        }

        //Validate o
        if (b.charAt(0) == 'o' && b.charAt(1) == 'o' && b.charAt(2) == 'o') {
            return "o";
        }
        if (b.charAt(3) == 'o' && b.charAt(4) == 'o' && b.charAt(5) == 'o') {
            return "o";
        }
        if (b.charAt(6) == 'o' && b.charAt(7) == 'o' && b.charAt(8) == 'o') {
            return "o";
        }

        if (b.charAt(0) == 'o' && b.charAt(3) == 'o' && b.charAt(6) == 'o') {
            return "o";
        }
        if (b.charAt(1) == 'o' && b.charAt(4) == 'o' && b.charAt(7) == 'o') {
            return "o";
        }
        if (b.charAt(2) == 'o' && b.charAt(5) == 'o' && b.charAt(8) == 'o') {
            return "o";
        }

        if (b.charAt(0) == 'o' && b.charAt(4) == 'o' && b.charAt(8) == 'o') {
            return "o";
        }
        if (b.charAt(2) == 'o' && b.charAt(4) == 'o' && b.charAt(6) == 'o') {
            return "o";
        }

        if (!this.getLineBoard().contains("-")) {
            return "d";
        }
        return "";
    }

    /**
     * Returns current board in a single line.<br>
     * Example:<br>
     * ["-","-","-"]<br>
     * ["-","-","-"]<br>
     * ["-","-","-"]<br>
     * is returned as:<br>
     * "--------"
     *
     * @return Board represented as a string.
     */
    public String getLineBoard() {
        String lineBoard = "";
        for (Space space : board) {
            lineBoard += space.getValue();
        }
        return lineBoard;
    }

    /**
     * Prints current Board to console.
     */
    public void printBoard() {
        int counter = 0;
        for (Space space : board) {
            if (counter % 3 == 0) {
                System.out.println();
            }
            System.out.print(space.getValue());
            counter++;
        }
        System.out.println();
    }
}
