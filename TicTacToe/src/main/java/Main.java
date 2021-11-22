import java.util.*;

public class Main {

    public static Scanner scanner;
    public static AI ai;
//    public static int trainingGames=1000000;
    public static void main(String[] args){
        ai = new AI(9);
//        for(int i=0;i<trainingGames;i++)
//        {
//            ai.simulateGame();
//        }
//        System.out.println("AI trained "+ai.trainingCounter+" times");
        scanner = new Scanner(System.in);
        int menu=1;
        do {
            playGameBrain();
            System.out.println("Would you like to play again?");
            menu=scanner.nextInt();
        }
        while(menu!=1000);
    }

    public static void playGame(){
        Board board = new Board();
        String humanPlayer ="x";
        board.setCurrentTurn(humanPlayer);

        String result="";
        while((result=board.validateWinner()).length()==0)
        {
            if(!board.getCurrentTurn().equals(humanPlayer)) {
                ArrayList<Double> weightsMoves= new ArrayList<>(Arrays.asList(new Double[9]));
                Collections.copy(weightsMoves,ai.predict(board.turnCount));
                ArrayList<Double> orderedWeights = new ArrayList<>(Arrays.asList(new Double[9]));
                Collections.copy(orderedWeights,weightsMoves);
                Collections.sort(orderedWeights,Collections.reverseOrder());
                System.out.println("move :"+weightsMoves.indexOf(orderedWeights.get(0)));
                System.out.println("best :"+orderedWeights.get(0));
                System.out.println("wmove :"+weightsMoves);
                boolean isValid=board.makeMove(board.currentTurn, weightsMoves.indexOf(orderedWeights.get(0)));
                orderedWeights.remove(0);
                while (!isValid) {
                    if(!orderedWeights.isEmpty()) {
                        System.out.println("move :"+weightsMoves.indexOf(orderedWeights.get(0)));
                        System.out.println("best :"+orderedWeights.get(0));
                        System.out.println("wmove :"+weightsMoves);
                        isValid = board.makeMove(board.currentTurn, weightsMoves.indexOf(orderedWeights.get(0)));
                        orderedWeights.remove(0);
                    }
                    else{
                        isValid = board.makeMove(board.currentTurn, new Random().nextInt(9));
                    }
                }
                System.out.println(weightsMoves);
                System.out.println(orderedWeights);
                board.printBoard();
            }else
            {
                System.out.println("Make your move");
                int input = scanner.nextInt();
                while (!board.makeMove(board.currentTurn, input)){
                    System.out.println("Invalid move make a new one");
                    input = scanner.nextInt();
                }
                board.printBoard();
            }
        }
        if (result.equals("x")){
            System.out.println("X won");
            System.out.println(board.getMoveXHistory());
                ai.trainBrain(board.getMoveXHistory(),board.getBoardHistory());
                if(ai.trainingCounter<999999999) {
                    ai.trainingCounter++;
                }else
                {
                    ai.trainingCounter=0;
                    ai.overlap++;
                }

        }else if(result.equals("o"))
        {
            System.out.println("O won");
            System.out.println(board.getMoveOHistory());
                ai.trainBrain(board.getMoveXHistory(),board.boardHistory);
                if(ai.trainingCounter<999999999) {
                    ai.trainingCounter++;
                }else
                {
                    ai.trainingCounter=0;
                    ai.overlap++;
                }

        }else if(result.equals("d"))
        {
            System.out.println("It's a draw");

        }
        board.printBoard();
        board.printBoardHistory();
    }

    public static void playGameBrain(){
        Board board = new Board();
        String humanPlayer ="x";
        board.setCurrentTurn(humanPlayer);

        String result="";
        while((result=board.validateWinner()).length()==0)
        {
            if(!board.getCurrentTurn().equals(humanPlayer)) {
                String game="";
                for(Board b: board.getBoardHistory())
                {
                    game+=b.printBoard(true);
                }
                ArrayList<Integer>moves = ai.predictBrain(game);
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
            }else
            {
                System.out.println("Make your move");
                int input = scanner.nextInt();
                while (!board.makeMove(board.currentTurn, input)){
                    System.out.println("Invalid move make a new one");
                    input = scanner.nextInt();
                }
            }
            board.printBoard();
        }
        if (result.equals("x")){
            System.out.println("X won");
            System.out.println(board.getMoveXHistory());
            ai.trainBrain(board.getMoveXHistory(),board.getBoardHistory());
            if(ai.trainingCounter<999999999) {
                ai.trainingCounter++;
            }else
            {
                ai.trainingCounter=0;
                ai.overlap++;
            }

        }else if(result.equals("o"))
        {
            System.out.println("O won");
            System.out.println(board.getMoveOHistory());
            ai.trainBrain(board.getMoveXHistory(),board.boardHistory);
            if(ai.trainingCounter<999999999) {
                ai.trainingCounter++;
            }else
            {
                ai.trainingCounter=0;
                ai.overlap++;
            }

        }else if(result.equals("d"))
        {
            System.out.println("It's a draw");

        }
        board.printBoard();
        board.printBoardHistory();
    }

}