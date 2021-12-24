import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AITest {

    @Test
    public void trainBrainTest(){
        AI ai = new AI();
        ArrayList<Integer> moves=new ArrayList(Arrays.asList(1));
        Board board = new Board();
        board.makeMove("x",1);
        ArrayList<Board> boardHistory=new ArrayList<>();
        boardHistory.add(board);
        ai.trainBrain(moves,boardHistory,0);
        boolean in=false;
        for (int i:ai.predictBrain(board.getLineBoard())){
            if(i==1){
                in=true;
            }
        }
        assertEquals(true,in);
    }

    @Test
    public void testPerformance() throws InterruptedException {
        AI ai = new AI();
        ai.loadBrain();
        ai.initTraining();
        ai.initTest();
        double perf = ai.currentPerformance;
        boolean acceptable=(perf>0.8)?true:false;
        boolean acceptableBest=(ai.bestPerformance>0)?true:false;
        ai.saveBrain();
        assertEquals(true,acceptable&&acceptableBest);
    }

}
