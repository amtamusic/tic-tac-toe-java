import lombok.SneakyThrows;

import java.util.*;

public class AI {

    ArrayList<InputLayer>layers;
    boolean isTraining;
    long trainingCounter=0;
    long overlap=0;
    Double bestPerformance =0.0;
    Double currentPerformance=0.0;
    Double testCases=100.0;
    Double testLossCounter=0.0;
    Double testWinCounter=0.0;
    Brain brain;

    public AI(int layers)
    {
        this.brain= new Brain();
        this.layers=new ArrayList<>();
        for(int i = 0 ; i < layers; i++)
        {
            this.layers.add(new InputLayer(9));
        }
        this.isTraining=true;
        BackgroundTrainer backgroundTrainer= new BackgroundTrainer();
        backgroundTrainer.start();
    }

    public void trainBrain(ArrayList<Integer>moves,ArrayList<Board>movesHistory)
    {
        int turn=0;
        String game;
        for (Integer move : moves)
        {
            if(move>=0) {
                game = movesHistory.get(turn).getLineBoard();
                brain.learn(game, move,true);
            }
            turn++;
        }
        //System.out.println("Finished testing");
    }

    public void trainBrainLoss(ArrayList<Integer>moves,ArrayList<Board>movesHistory)
    {
                String game = movesHistory.get(movesHistory.size()-2).getLineBoard();
                brain.learn(game, moves.get(moves.size()-1),false);
    }


    public void test()
    {
        for(int i =0;i<testCases;i++)
        {
            simulateGameBrain(true);
        }
        //System.out.println("Tests ran "+(testWinCounter+testLossCounter));
        currentPerformance=testWinCounter/testCases;
        testWinCounter=0.0;
        testLossCounter=0.0;
    }

    public ArrayList<Integer> predictBrain(String game)
    {
        HashSet<Integer> hash=brain.remember(game);
        if(hash==null)
        {
            hash= new HashSet<>();
        }
        return new ArrayList<>();
    }

    public void simulateGameBrain(boolean isTest)
    {
        Board board = new Board();
        String result;
        while((result=board.validateWinner()).length()==0)
        {
            boolean isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
            while (!isValid) {
                    isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
            }
        }
        if (result.equals("x")){
            //System.out.println("X won");
            //System.out.println(board.getMoveXHistory());
            if(isTraining&&!isTest)
            {
                //System.out.println("X win: "+board.getLineBoard()+board.moveOHistory+board.moveXHistory);
              //  trainBrain(board.getMoveXHistory(),board.getBoardHistory());
//                if(trainingCounter<999999999) {
//                    trainingCounter++;
//                }else
//                {
//                    trainingCounter=0;
//                    overlap++;
//                }

            }

            if(isTest)
            {
                testLossCounter++;
            }
        }else if(result.equals("o"))
        {
            //System.out.println("O won");
            //System.out.println(board.getMoveOHistory());
            if(isTraining && !isTest)
            {
                trainBrain(board.getMoveOHistory(),board.boardHistory);
                if(trainingCounter<999999999) {
                    trainingCounter++;
                }else
                {
                    trainingCounter=0;
                    overlap++;
                }
            }

            if(isTest)
            {
                testWinCounter++;
            }
        }else if(result.equals("d"))
        {
            //System.out.println("Draw: "+board.getLineBoard()+board.moveOHistory+board.moveXHistory);
            //trainBrain(board.getMoveXHistory(),board.boardHistory);
            trainBrain(board.getMoveOHistory(),board.boardHistory);
            //System.out.println("It's a draw");
            if(isTest)
            {
                testWinCounter++;
            }
        }else
        {
            trainBrainLoss(board.getMoveOHistory(),board.boardHistory);
            if(trainingCounter<999999999) {
                trainingCounter++;
            }else
            {
                trainingCounter=0;
                overlap++;
            }
            if(isTest)
            {
                testLossCounter++;
            }
        }
        //board.printBoard();
        //board.printBoardHistory();
    }

    public void initTest()
    {
        test();
        if(currentPerformance> bestPerformance)
        {
            bestPerformance =currentPerformance;
        }
        System.out.println("Current perf:"+ bestPerformance);
        //System.out.println("Done testing iteration");
    }

    public void initTraining()
    {
        while (trainingCounter<1000000)
        {
            simulateGameBrain(false);
        }
        //System.out.println("Training ran "+trainingCounter+" times.");
        //System.out.println("Current games in memory "+brain.getMemory().size());
        trainingCounter=0;
    }

    public void initTrainingAndTesting()
    {
        initTraining();
        initTest();
    }

    public class BackgroundTrainer extends Thread{
        @Override
        public void run() {
            while(true)
            {initTrainingAndTesting();}
        }
    }
}
