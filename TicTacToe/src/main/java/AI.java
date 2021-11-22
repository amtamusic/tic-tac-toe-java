import lombok.SneakyThrows;

import java.util.*;

public class AI {

    ArrayList<InputLayer>layers;
    Double trueValue=1.05;
    Double passiveValue=1.012;
    Double wrongValue=0.95;
    boolean isTraining;
    long trainingCounter=0;
    long overlap=0;
    Double bestPerformanceX =0.0;
    Double bestPerformanceY =0.0;
    Double currentPerformance=0.0;
    Double testCases=1000.0;
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
       // BackgroundTester backgroundTester = new BackgroundTester();
        //backgroundTester.start();
    }

//    public void train(ArrayList<Integer>moves,ArrayList<Board>movesHistory)
//    {
//        int turn=0;
//        ArrayList<InputLayer>inputLayerRollback= new ArrayList<>(Arrays.asList(new InputLayer[9]));
//        Collections.copy(inputLayerRollback,layers);
//        for (Integer move : moves)
//        {
//            ArrayList<Double>trainweights=new ArrayList<>(Arrays.asList(new Double[]{wrongValue,wrongValue,wrongValue,wrongValue,wrongValue,wrongValue,wrongValue,wrongValue,wrongValue}));
//            if(move>=0) {
//                if(turn%2==1) {
//                    trainweights.set(move, trueValue);
//                    Board board = movesHistory.get(turn);
//                    for(int i =0;i<board.getBoard().size();i++)
//                    {
//                        if(i!=move)
//                        {
//                            if(board.getBoard().get(i).getValue().equals("o")) {
//                                trainweights.set(i, passiveValue);
//                            }
//                        }
//                    }
//                    layers.get(turn).updateWeights(trainweights);
//                    test();
//                    if(currentPerformance> bestPerformanceX)
//                    {
//                        bestPerformanceX =currentPerformance;
//                        System.out.println("Current perf X:"+ bestPerformanceX);
//                    }else
//                    {
//                        Collections.copy(layers,inputLayerRollback);
//                    }
//                }else
//                {
//                    trainweights.set(move, trueValue);
//                    Board board = movesHistory.get(turn);
//                    for(int i =0;i<board.getBoard().size();i++)
//                    {
//                        if(i!=move)
//                        {
//                            if(board.getBoard().get(i).getValue().equals("x")) {
//                                trainweights.set(i, passiveValue);
//                            }
//                        }
//                    }
//                    layers.get(turn).updateWeights(trainweights);
//                    test();
//                    if(currentPerformance> bestPerformanceY)
//                    {
//                        bestPerformanceY =currentPerformance;
//                        System.out.println("Current perf Y:"+ bestPerformanceX);
//                    }else
//                    {
//                        Collections.copy(layers,inputLayerRollback);
//                    }
//                }
//            }
//            turn++;
//        }
//        //System.out.println("Finished testing");
//    }

    public void trainBrain(ArrayList<Integer>moves,ArrayList<Board>movesHistory)
    {
        int turn=0;
        String game="";
        for (Integer move : moves)
        {
            game+=movesHistory.get(turn);
            brain.learn(game,move);
            turn++;
        }
        //System.out.println("Finished testing");
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

    public ArrayList<Double> predict(int move)
    {
        ArrayList<Double>weights;
        weights= layers.get(move).getWeights();
        return weights;
    }

    public ArrayList<Integer> predictBrain(String game)
    {
        return brain.remember(game);
    }

    public void simulateGameBrain(boolean isTest)
    {
        Board board = new Board();
        String result;
        while((result=board.validateWinner()).length()==0)
        {
            String game="";
            for(Board b: board.getBoardHistory())
            {
                game+=b.printBoard(true);
            }
            ArrayList<Integer>moves = predictBrain(game);
            int choice = -1;
            boolean isValid;
            if(moves!=null && !moves.isEmpty()) {
                choice = new Random().nextInt(moves.size());
                isValid=board.makeMove(board.currentTurn,moves.get(choice));
                moves.remove(choice);
            }else
            {
                isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
            }

            while (!isValid) {
                if(moves!=null && !moves.isEmpty()) {
                    choice = new Random().nextInt(moves.size());
                    isValid = board.makeMove(board.currentTurn,moves.get(choice));
                    moves.remove(choice);
                }
                else{
                    isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                }
            }
        }
        if (result.equals("x")){
            //System.out.println("X won");
            //System.out.println(board.getMoveXHistory());
            if(isTraining&&!isTest)
            {
                trainBrain(board.getMoveXHistory(),board.getBoardHistory());
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
                testLossCounter++;
            }
        }else if(result.equals("o"))
        {
            //System.out.println("O won");
            //System.out.println(board.getMoveOHistory());
            if(isTraining && !isTest)
            {
                trainBrain(board.getMoveXHistory(),board.boardHistory);
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
            //System.out.println("It's a draw");
            if(isTest)
            {
                testWinCounter++;
            }
        }else
        {
            if(isTest)
            {
                testLossCounter++;
            }
        }
        //board.printBoard();
        //board.printBoardHistory();
    }

//    public void simulateGame(boolean isTest)
//    {
//        Board board = new Board();
//        String result="";
//        while((result=board.validateWinner()).length()==0)
//        {
////            while (!board.makeMove(board.currentTurn,new Random().nextInt(9)))
////            {
////                //System.out.println("invalid move");
////            }
//            ArrayList<Double> weightsMoves= new ArrayList<>(Arrays.asList(new Double[9]));
//            Collections.copy(weightsMoves,predict(board.turnCount));
//            ArrayList<Double> orderedWeights = new ArrayList<>(Arrays.asList(new Double[9]));
//            Collections.copy(orderedWeights,weightsMoves);
//            Collections.sort(orderedWeights,Collections.reverseOrder());
//
//            boolean isValid=board.makeMove(board.currentTurn, weightsMoves.indexOf(orderedWeights.get(0)));
//            orderedWeights.remove(0);
//            while (!isValid) {
//                if(!orderedWeights.isEmpty()) {
//                    isValid = board.makeMove(board.currentTurn, weightsMoves.indexOf(orderedWeights.get(0)));
//                    orderedWeights.remove(0);
//                }
//                else{
//                    isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
//                }
//            }
//        }
//        if (result.equals("x")){
//            //System.out.println("X won");
//            //System.out.println(board.getMoveXHistory());
//            if(isTraining&&!isTest)
//            {
//                trainBrain(board.getMoveXHistory(),board.getBoardHistory());
//                if(trainingCounter<999999999) {
//                    trainingCounter++;
//                }else
//                {
//                    trainingCounter=0;
//                    overlap++;
//                }
//
//            }
//
//            if(isTest)
//            {
//                testLossCounter++;
//            }
//        }else if(result.equals("o"))
//        {
//            //System.out.println("O won");
//            //System.out.println(board.getMoveOHistory());
//            if(isTraining && !isTest)
//            {
//                trainBrain(board.getMoveXHistory(),board.boardHistory);
//                if(trainingCounter<999999999) {
//                    trainingCounter++;
//                }else
//                {
//                    trainingCounter=0;
//                    overlap++;
//                }
//            }
//
//            if(isTest)
//            {
//                testWinCounter++;
//            }
//        }else if(result.equals("d"))
//        {
//            //System.out.println("It's a draw");
//            if(isTest)
//            {
//                testWinCounter++;
//            }
//        }else
//        {
//            if(isTest)
//            {
//                testLossCounter++;
//            }
//        }
//        //board.printBoard();
//        //board.printBoardHistory();
//    }

    public class BackgroundTrainer extends Thread
    {
        @Override
        public void run()
        {
            while (true)
            {
                simulateGameBrain(false);
                if(trainingCounter%100000==0)
                {
                    System.out.println("Training reached "+trainingCounter+" times.");
                    System.out.println("Current games in memory "+brain.getMemory().size());
                }
            }
        }
    }

    public class BackgroundTester extends Thread
    {
        @SneakyThrows
        @Override
        public void run()
        {
            while (true)
            {
                test();
                if(currentPerformance> bestPerformanceY)
                {
                    bestPerformanceY =currentPerformance;
                    System.out.println("Current perf:"+ bestPerformanceY);
                }
            }
        }
    }
}
