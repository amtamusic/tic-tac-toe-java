package andytech.ai;

import andytech.game.Board;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Author: Andres Toledo
 * This classes represents an AI algorithm to play tic-tac-toe
 *
 * @author amtam
 * @version $Id: $Id
 */
@Data
public class AI {

    /**
     * AI memory.
     */
    public static Brain brain;
    /**
     * Toggle Training mode.
     */
    boolean isTraining;
    /**
     * Training iteration counter.
     */
    long trainingCounter;
    /**
     * If training surpasses long size.
     */
    long overlap;
    /**
     * AI best performance percentage.
     */
    Double bestPerformance;
    /**
     * AI performance current iteration.
     */
    Double currentPerformance;
    /**
     * Toggle number of tests per training session.
     */
    Double testCases;
    /**
     * Test batch losses.
     */
    Double testLossCounter;
    /**
     * Test batch wins.
     */
    Double testWinCounter;
    /**
     * Toggle number of training games per run.
     */
    Double trainingGames;

    /**
     * Initialize AI variables.
     */
    public AI() {
        brain = new Brain();
        this.isTraining = true;
        this.trainingCounter = 0;
        this.overlap = 0;
        this.bestPerformance = 0.0;
        this.currentPerformance = 0.0;
        this.testCases = 100.0;
        this.testLossCounter = 0.0;
        this.testWinCounter = 0.0;
        this.trainingGames = 1000000.0;
        //Start background trainer on separate Thread
        BackgroundTrainer backgroundTrainer = new BackgroundTrainer();
        backgroundTrainer.start();
    }

    /**
     * Trains AI after a win or draw for every move made until it got to a victory.
     *
     * @param moves        Array representing moves in current game.
     * @param movesHistory Array representing the boards after each move in the game.
     * @param player       Current player training by default only 0 is used in current implementation since AI only plays o.
     */
    public void trainBrain(ArrayList<Integer> moves, ArrayList<Board> movesHistory, int player) {
        int turn = 0;
        String game;
        for (Integer move : moves) {
            if (move >= 0) {
                game = movesHistory.get(turn).getLineBoard();
                brain.learn(game, move, true, player);
            }
            turn++;
        }
    }

    /**
     * Teaches AI how to block the move that caused it to lose. This introduces the concept of defensive and offensive play.
     *
     * @param moves        Array representing moves in current game.
     * @param movesHistory Array representing the boards after each move in the game.
     */
    public void trainBrainLoss(ArrayList<Integer> moves, ArrayList<Board> movesHistory) {
        String game = movesHistory.get(movesHistory.size() - 3).getLineBoard();
        brain.learn(game, moves.get(moves.size() - 1), false, 0);
    }

    /**
     * Tests ai simulating a defined number of games by the variable {@link andytech.ai.AI#testCases}
     */
    public void test() {
        for (int i = 0; i < testCases; i++) {
            simulateGameBrain(true, this);
        }
        currentPerformance = testWinCounter / testCases;
        testWinCounter = 0.0;
        testLossCounter = 0.0;
    }

    /**
     * Ask AI to move giving it a board in a string format:<br>
     * <b>Example:</b>xx-oo----
     *
     * @param game board in string format.
     * @return Array of possible moves, returns empty if none are available.
     */
    public ArrayList<Integer> predictBrain(String game) {
        //Looks through memory if it does not remember the play returns empty array
        HashSet<Integer> hash = brain.remember(game);
        if (hash == null) {
            hash = new HashSet<>();
        }
        return new ArrayList<>(hash);
    }

    /**
     * Simulates a game between AI and random plays.
     *
     * @param isTest Flag used to specify if simulated game is used for training or testing.
     * @param ai     AI to simulate game with.
     */
    public void simulateGameBrain(boolean isTest, AI ai) {
        //Create new Board.
        Board board = new Board();
        //Set x as random moves and move first.
        String result;
        String humanPlayer = "x";
        board.setCurrentTurn(humanPlayer);
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
                //Random moves to help train AI.
                boolean isValid = board.makeMove(board.getCurrentTurn(), new Random().nextInt(9));
                while (!isValid) {
                    isValid = board.makeMove(board.getCurrentTurn(), new Random().nextInt(9));
                }
            }
        }
        //Random moves won.
        if (result.equals("x")) {
            if (isTraining && !isTest) {
                //Train Loss.
                trainBrainLoss(board.getMoveXHistory(), board.getBoardHistory());
                if (trainingCounter < 999999999) {
                    trainingCounter++;
                } else {
                    trainingCounter = 0;
                    overlap++;
                }
            }

            if (isTest) {
                testLossCounter++;
            }
            //AI won.
        } else if (result.equals("o")) {
            if (isTraining && !isTest) {
                //Train Win.
                trainBrain(board.getMoveOHistory(), board.getBoardHistory(), 0);
                if (trainingCounter < 999999999) {
                    trainingCounter++;
                } else {
                    trainingCounter = 0;
                    overlap++;
                }
            }

            if (isTest) {
                testWinCounter++;
            }
            //If draw
        } else if (result.equals("d")) {
            //Train Draw.
            trainBrain(board.getMoveOHistory(), board.getBoardHistory(), 0);
            if (isTest) {
                testWinCounter++;
            }
        }
    }

    /**
     * Initializes tests and updates {@link andytech.ai.AI#bestPerformance} if new best is found.
     */
    public void initTest() {
        test();
        if (currentPerformance > bestPerformance) {
            bestPerformance = currentPerformance;
        }
    }

    /**
     * Trains AI with number of test cases defined by {@link andytech.ai.AI#testCases}.
     */
    public void initTraining() {
        while (trainingCounter < trainingGames) {
            simulateGameBrain(false, this);
        }
        trainingCounter = 0;
    }

    /**
     * Initializes training and testing simultaneously by calling {@link andytech.ai.AI#initTraining()},{@link andytech.ai.AI#initTest()}.
     */
    public void initTrainingAndTesting() {
        initTraining();
        initTest();
    }

    /**
     * Saves current brain in tictac.brain file in current working directory.
     *
     * @return Status of save operation.
     */
    public boolean saveBrain() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tictac.brain"));
            oos.writeObject(brain);
            oos.flush();
            oos.close();
            System.out.println("Saved Brain");
            return true;
        } catch (IOException e) {
            System.out.println("Error Saving Brain : " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads saved brain from tictac.brain file in current working directory.
     *
     * @return Status of load operation.
     */
    public boolean loadBrain() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tictac.brain"));
            brain = (Brain) ois.readObject();
            ois.close();
            System.out.println("Loaded Brain");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error Loading Brain : " + e.getMessage());
            return false;
        }
    }

    /**
     * Print current best performance.
     */
    public void printCurrentPerformance() {
        System.out.println("Current perf: " + bestPerformance);
    }

    /**
     * Trains AI in background after application is started.
     */
    public class BackgroundTrainer extends Thread {
        /**
         * Initializes training and testing by calling {@link AI#initTrainingAndTesting()}.
         */
        @Override
        public void run() {
            while (true) {
                initTrainingAndTesting();
            }
        }
    }
}
