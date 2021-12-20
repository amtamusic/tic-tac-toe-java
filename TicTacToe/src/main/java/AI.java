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

    public void trainBrain(ArrayList<Integer>moves,ArrayList<Board>movesHistory,int player)
    {
        int turn=0;
        String game;
        for (Integer move : moves)
        {
            if(move>=0) {
                game = movesHistory.get(turn).getLineBoard();
                brain.learn(game, move,true,player);
            }
            turn++;
        }
        //System.out.println("Finished testing");
    }

    public void trainBrainLoss(ArrayList<Integer>moves,ArrayList<Board>movesHistory)
    {
                String game = movesHistory.get(movesHistory.size()-2).getLineBoard();
                brain.learnNN(game, moves.get(moves.size()-1),0);
    }


    public void test()
    {
        for(int i =0;i<testCases;i++)
        {
            simulateGameBrain(true,this);
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
        return new ArrayList<>(hash);
    }

    public  ArrayList<Integer> predictNN(String game,int player){
        ArrayList<String> a = new ArrayList<>(Arrays.asList(game.split("")));
        double[] f = new double[a.size()+1];
        for(int i =0;i<a.size();i++){
            f[i]=Double.parseDouble(a.get(i).replace("-","0").replace("x","1").replace("o","0"));
        }
        f[f.length-1]=player;
        ArrayList<Integer> result = new ArrayList<>();
        List<Double> output = brain.getNn().predict(f);
        for(int i=0;i<9;i++)
        {
            if(output.get(i)>0.5){
                result.add(i);
            }
        }
        return result;
    }

    public void simulateGameBrain(boolean isTest,AI ai)
    {
        Board board = new Board();
        String result;
        String humanPlayer ="x";
        board.setCurrentTurn(humanPlayer);
        while((result=board.validateWinner()).length()==0)
        {
            if(!board.getCurrentTurn().equals(humanPlayer)) {
                ArrayList<Integer> moves = ai.predictNN(board.getLineBoard(), 0);
                int choice = -1;
                boolean isValid;
                if (moves != null && !moves.isEmpty()) {
                    //Double max= Collections.max(moves);
                    choice = new Random().nextInt(moves.size());
                    //choice = moves.indexOf(max);
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
            }else
            {

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
                trainBrain(board.getMoveOHistory(),board.boardHistory,0);
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
            trainBrain(board.getMoveOHistory(),board.boardHistory,0);
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
        while (trainingCounter<1000)
        {
            simulateGameBrain(false,this);
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
