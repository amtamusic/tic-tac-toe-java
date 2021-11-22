import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

    Board board;

    @Test
    public void testXWinCondition1()
    {
        board= new Board();
        board.makeMove("x",0);
        board.makeMove("x",1);
        board.makeMove("x",2);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition2()
    {
        board= new Board();
        board.makeMove("x",3);
        board.makeMove("x",4);
        board.makeMove("x",5);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition3()
    {
        board= new Board();
        board.makeMove("x",6);
        board.makeMove("x",7);
        board.makeMove("x",8);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition4()
    {
        board= new Board();
        board.makeMove("x",0);
        board.makeMove("x",3);
        board.makeMove("x",6);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition5()
    {
        board= new Board();
        board.makeMove("x",1);
        board.makeMove("x",4);
        board.makeMove("x",7);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition6()
    {
        board= new Board();
        board.makeMove("x",2);
        board.makeMove("x",5);
        board.makeMove("x",8);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition7()
    {
        board= new Board();
        board.makeMove("x",0);
        board.makeMove("x",4);
        board.makeMove("x",8);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testXWinCondition8()
    {
        board= new Board();
        board.makeMove("x",2);
        board.makeMove("x",4);
        board.makeMove("x",6);
        assertEquals("x",board.validateWinner());
    }

    @Test
    public void testOWinCondition1()
    {
        board= new Board();
        board.makeMove("o",0);
        board.makeMove("o",1);
        board.makeMove("o",2);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition2()
    {
        board= new Board();
        board.makeMove("o",3);
        board.makeMove("o",4);
        board.makeMove("o",5);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition3()
    {
        board= new Board();
        board.makeMove("o",6);
        board.makeMove("o",7);
        board.makeMove("o",8);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition4()
    {
        board= new Board();
        board.makeMove("o",0);
        board.makeMove("o",3);
        board.makeMove("o",6);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition5()
    {
        board= new Board();
        board.makeMove("o",1);
        board.makeMove("o",4);
        board.makeMove("o",7);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition6()
    {
        board= new Board();
        board.makeMove("o",2);
        board.makeMove("o",5);
        board.makeMove("o",8);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition7()
    {
        board= new Board();
        board.makeMove("o",0);
        board.makeMove("o",4);
        board.makeMove("o",8);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void testOWinCondition8()
    {
        board= new Board();
        board.makeMove("o",2);
        board.makeMove("o",4);
        board.makeMove("o",6);
        assertEquals("o",board.validateWinner());
    }

    @Test
    public void noWinner()
    {
        board= new Board();
        assertEquals("",board.validateWinner());
    }

    @Test
    public void draw()
    {
        board= new Board();
        board.makeMove("x",0);
        board.makeMove("x",1);
        board.makeMove("y",2);
        board.makeMove("y",3);
        board.makeMove("x",4);
        board.makeMove("x",5);
        board.makeMove("x",6);
        board.makeMove("y",7);
        board.makeMove("y",8);

        assertEquals("d",board.validateWinner());
    }
}
