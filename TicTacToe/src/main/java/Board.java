import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Data
public class Board {
    ArrayList<Space>board;
    ArrayList<Player>players;
    String currentTurn;
    int turnCount;
    ArrayList<Board>boardHistory;
    ArrayList<Integer>moveXHistory;
    ArrayList<Integer> moveOHistory;
    public Board()
    {
        this.board= new ArrayList<>();
        for (int i=0;i<9;i++)
        {
            board.add(new Space("-"));
        }
        players= new ArrayList<>();
        players.add(new Player("x"));
        players.add(new Player("o"));
        currentTurn= players.get(new Random().nextInt(players.size())).name;
        boardHistory=new ArrayList<>();
        moveXHistory=new ArrayList<>();
        moveOHistory =new ArrayList<>();
    }

    public void nextTurn()
    {
        if(currentTurn.equalsIgnoreCase("x"))
        {
            currentTurn="o";
        }else
        {
            currentTurn="x";
        }
        turnCount++;
    }

    public boolean validMove(int position)
    {
        if(!board.get(position).getValue().equals("-"))
        {
            return false;
        }
        return true;
    }
    
    public boolean makeMove(String value,int position)
    {
        if(moveXHistory.isEmpty()&&moveOHistory.isEmpty())
        {
            boardHistory.add(copyBoard());
        }
        if(validMove(position)) {
            board.set(position, new Space(value));
            boardHistory.add(copyBoard());
            if(value.equals("x"))
            {
                moveXHistory.add(position);
                moveOHistory.add(-1);
            }
            if(value.equals("o"))
            {
                moveOHistory.add(position);
                moveXHistory.add(-1);
            }
            nextTurn();
            return true;
        }
        return false;
    }

    public Board copyBoard()
    {
        Board temp = new Board();
        for(int i=0;i< this.board.size();i++)
        {
            temp.getBoard().get(i).setValue(this.getBoard().get(i).getValue());
        }
        return temp;
    }

    public String validateWinner()
    {
        ArrayList<String>winConditions=new ArrayList<>();
        winConditions.add("www.{6}");
        winConditions.add(".{3}www.{3}");
        winConditions.add(".{6}www");
        winConditions.add("w.{2}w.{2}w.{2}");
        winConditions.add(".{1}w.{1}.{1}w.{1}.{1}w.{1}");
        winConditions.add(".{2}w.{2}w.{2}w");
        winConditions.add("w.{1}.{1}.{1}w.{1}.{1}.{1}w");
        winConditions.add(".{1}.{1}w.{1}w.{1}w.{1}.{1}");

        for (String winCondition : winConditions)
        {
            if(this.getLineBoard().replaceAll(winCondition.replaceAll("w","x"),"").length()==0)
            {
                return "x";
            }else if(this.getLineBoard().replaceAll(winCondition.replaceAll("w","o"),"").length()==0)
            {
                return "o";
            }
        }
        if(!this.getLineBoard().contains("-"))
        {
            return "d";
        }
        return "";
    }
    
    public String getLineBoard()
    {
        String lineBoard="";
        for(Space space: board)
        {
            lineBoard+=space.getValue();
        }
        //System.out.println(lineBoard);
        return lineBoard;
    }

    public void printBoard()
    {
        int counter=0;
        for (Space space: board)
        {
            if(counter%3==0)
            {
                System.out.println();
            }
            System.out.print(space.getValue());
            counter++;
        }
        System.out.println();
    }

    public String printBoard(boolean oneRow)
    {
        String result="";
        for (Space space: board)
        {
            result+=space.getValue();
        }
        return result;
    }

    public void printBoardHistory()
    {
        int counter=0;
        for (Board board: boardHistory)
        {
            board.printBoard();
            System.out.println("/n turn:"+ counter);
            counter++;
        }
        System.out.println();
        System.out.println();
    }
}
