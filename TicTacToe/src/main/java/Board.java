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
        if(validMove(position)) {
            if(moveXHistory.isEmpty()&&moveOHistory.isEmpty())
            {
                boardHistory.add(copyBoard());
            }
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
        String b =this.getLineBoard();
        //validate x
        if(b.charAt(0)=='x'&&b.charAt(1)=='x'&&b.charAt(2)=='x')
        {
            return "x";
        }
        if(b.charAt(3)=='x'&&b.charAt(4)=='x'&&b.charAt(5)=='x')
        {
            return "x";
        }
        if(b.charAt(6)=='x'&&b.charAt(7)=='x'&&b.charAt(8)=='x')
        {
            return "x";
        }

        if(b.charAt(0)=='x'&&b.charAt(3)=='x'&&b.charAt(6)=='x')
        {
            return "x";
        }
        if(b.charAt(1)=='x'&&b.charAt(4)=='x'&&b.charAt(7)=='x')
        {
            return "x";
        }
        if(b.charAt(2)=='x'&&b.charAt(5)=='x'&&b.charAt(8)=='x')
        {
            return "x";
        }

        if(b.charAt(0)=='x'&&b.charAt(4)=='x'&&b.charAt(8)=='x')
        {
            return "x";
        }
        if(b.charAt(2)=='x'&&b.charAt(4)=='x'&&b.charAt(6)=='x')
        {
            return "x";
        }

        //Validate o
        if(b.charAt(0)=='o'&&b.charAt(1)=='o'&&b.charAt(2)=='o')
        {
            return "o";
        }
        if(b.charAt(3)=='o'&&b.charAt(4)=='o'&&b.charAt(5)=='o')
        {
            return "o";
        }
        if(b.charAt(6)=='o'&&b.charAt(7)=='o'&&b.charAt(8)=='o')
        {
            return "o";
        }

        if(b.charAt(0)=='o'&&b.charAt(3)=='o'&&b.charAt(6)=='o')
        {
            return "o";
        }
        if(b.charAt(1)=='o'&&b.charAt(4)=='o'&&b.charAt(7)=='o')
        {
            return "o";
        }
        if(b.charAt(2)=='o'&&b.charAt(5)=='o'&&b.charAt(8)=='o')
        {
            return "o";
        }

        if(b.charAt(0)=='o'&&b.charAt(4)=='o'&&b.charAt(8)=='o')
        {
            return "o";
        }
        if(b.charAt(2)=='o'&&b.charAt(4)=='o'&&b.charAt(6)=='o')
        {
            return "o";
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
