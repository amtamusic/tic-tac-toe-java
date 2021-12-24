import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class AI {

    public static Brain brain;
    boolean isTraining;
    long trainingCounter = 0;
    long overlap = 0;
    Double bestPerformance = 0.0;
    Double currentPerformance = 0.0;
    Double testCases = 100.0;
    Double testLossCounter = 0.0;
    Double testWinCounter = 0.0;

    public AI() {
        brain = new Brain();
        this.isTraining = true;
        BackgroundTrainer backgroundTrainer = new BackgroundTrainer();
        backgroundTrainer.start();
    }

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

    public void trainBrainLoss(ArrayList<Integer> moves, ArrayList<Board> movesHistory) {
        String game = movesHistory.get(movesHistory.size() - 3).getLineBoard();
        brain.learn(game, moves.get(moves.size() - 1), false, 0);
    }


    public void test() {
        for (int i = 0; i < testCases; i++) {
            simulateGameBrain(true, this);
        }
        currentPerformance = testWinCounter / testCases;
        testWinCounter = 0.0;
        testLossCounter = 0.0;
    }

    public ArrayList<Integer> predictBrain(String game) {
        HashSet<Integer> hash = brain.remember(game);
        if (hash == null) {
            hash = new HashSet<>();
        }
        return new ArrayList<>(hash);
    }

    public void simulateGameBrain(boolean isTest, AI ai) {
        Board board = new Board();
        String result;
        String humanPlayer = "x";
        board.setCurrentTurn(humanPlayer);
        while ((result = board.validateWinner()).length() == 0) {
            if (!board.getCurrentTurn().equals(humanPlayer)) {
                ArrayList<Integer> moves = ai.predictBrain(board.getLineBoard());
                int choice = -1;
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
                boolean isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                while (!isValid) {
                    isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                }
            }
        }
        if (result.equals("x")) {
            if (isTraining && !isTest) {
                trainBrainLoss(board.getMoveXHistory(), board.boardHistory);
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
        } else if (result.equals("o")) {
            if (isTraining && !isTest) {
                trainBrain(board.getMoveOHistory(), board.boardHistory, 0);
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
        } else if (result.equals("d")) {
            trainBrain(board.getMoveOHistory(), board.boardHistory, 0);
            if (isTest) {
                testWinCounter++;
            }
        }
    }

    public void initTest() {
        test();
        if (currentPerformance > bestPerformance) {
            bestPerformance = currentPerformance;
        }
        System.out.println("Current perf:" + bestPerformance);
    }

    public void initTraining() {
        while (trainingCounter < 1000000) {
            simulateGameBrain(false, this);
        }
        trainingCounter = 0;
    }

    public void initTrainingAndTesting() {
        initTraining();
        initTest();
    }

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

    public class BackgroundTrainer extends Thread {
        @Override
        public void run() {
            while (true) {
                initTrainingAndTesting();
            }
        }
    }
}
