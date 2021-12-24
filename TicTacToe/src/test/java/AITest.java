import andytech.ai.AI;
import andytech.game.Board;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AITest {

    @Test
    public void trainBrainTest() {
        AI ai = new AI();
        ArrayList<Integer> moves = new ArrayList(Arrays.asList(1));
        Board board = new Board();
        board.makeMove("x", 1);
        ArrayList<Board> boardHistory = new ArrayList<>();
        boardHistory.add(board);
        ai.trainBrain(moves, boardHistory, 0);
        boolean in = false;
        for (int i : ai.predictBrain(board.getLineBoard())) {
            if (i == 1) {
                in = true;
            }
        }
        assertEquals(true, in);
    }

    @Test
    public void testPerformance() {
        AI ai = new AI();
        ai.loadBrain();
        ai.initTraining();
        ai.initTest();
        double perf = ai.getCurrentPerformance();
        boolean acceptable = (perf > 0.7) ? true : false;
        boolean acceptableBest = (ai.getBestPerformance() > 0) ? true : false;
        ai.saveBrain();
        assertEquals(true, acceptable && acceptableBest);
    }

}
