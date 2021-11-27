import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
public class Brain {
    HashMap<String, HashSet<Integer>>memoryOffensive;
    HashMap<String, HashSet<Integer>>memoryDefensive;
    public Brain()
    {
        this.memoryOffensive=new HashMap<>();
        this.memoryDefensive=new HashMap<>();
    }

    public void learn(String game, int move,boolean isOffense)
    {
        if(isOffense)
        {
            learn(game,move,this.memoryOffensive);
        }else
        {
            learn(game,move,this.memoryDefensive);
        }
    }

    public void learn(String game, int move,HashMap<String, HashSet<Integer>>memory)
    {
        HashSet<Integer> moves;
        if(!memory.containsKey(game))
        {
            //System.out.println(game);
            moves = new HashSet<>();
        }else
        {
            moves = memory.get(game);
            //System.out.println("Game Found: "+game+" : "+moves.size());
        }
        moves.add(move);
        memory.put(game,moves);
    }

    public HashSet<Integer> remember(String game)
    {
        HashSet<Integer>memoryDefensive=this.memoryDefensive.get(game);
        if(memoryDefensive==null || memoryDefensive.isEmpty())
        {
            return this.memoryOffensive.get(game);
        }
        return memoryDefensive;
    }
}
